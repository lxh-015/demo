package com.linxh.paas.performance.controller;

import com.google.gson.JsonObject;
import com.linxh.paas.performance.enums.MilvusVersionEnum;
import io.milvus.client.CollectionMapping;
import io.milvus.client.ConnectParam;
import io.milvus.client.GetIndexInfoResponse;
import io.milvus.client.HasCollectionResponse;
import io.milvus.client.Index;
import io.milvus.client.IndexType;
import io.milvus.client.InsertParam;
import io.milvus.client.InsertResponse;
import io.milvus.client.MetricType;
import io.milvus.client.MilvusClient;
import io.milvus.client.MilvusGrpcClient;
import io.milvus.client.Response;
import io.milvus.client.SearchParam;
import io.milvus.client.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.Collectors;

/**
 * @author lin
 */
@Slf4j
@RestController
public class MilvusController {


    /**
     * milvus 集群地址
     */

    private static final MilvusVersionEnum MILVUS_VERSION = MilvusVersionEnum.V1_1_0;
    /**
     * 集合名称
     */
    private static final String MILVUS_COLLECTION_NAME = "test_20210625";
    /**
     * 每个向量 维度
     */
    private static final int MILVUS_DIMENSION = 512;
    /**
     * 每个索引文件最大值(MB)
     */
    private static final int MILVUS_INDEX_FILE_SIZE = 1024;
    /**
     * 使用 IP 作为度量类型
     */
    private static final MetricType METRIC_TYPE = MetricType.IP;
    /**
     * 使用 IVF_SQ8 作为索引类型
     */
    private static final IndexType indexType = IndexType.IVF_SQ8;
    private MilvusClient milvusClient;

    @PostConstruct
    public void postConstruct() {
        ConnectParam connectParam = new ConnectParam
                .Builder().withHost(MILVUS_VERSION.getHost()).withPort(MILVUS_VERSION.getPort()).build();
        milvusClient = new MilvusGrpcClient(connectParam);
    }

    @GetMapping("/api/v1/performance/milvus/collections/action-exist")
    public boolean hasCollection() {
        HasCollectionResponse response = milvusClient.hasCollection(MILVUS_COLLECTION_NAME);
        return response.hasCollection();
    }

    @PostMapping("/api/v1/performance/milvus/collections")
    public boolean createCollection() {
        CollectionMapping collectionMapping = new CollectionMapping.Builder(MILVUS_COLLECTION_NAME, MILVUS_DIMENSION)
                .withIndexFileSize(MILVUS_INDEX_FILE_SIZE)
                .withMetricType(METRIC_TYPE)
                .build();
        Response response = milvusClient.createCollection(collectionMapping);
        return response.ok();
    }

    @PostMapping("/api/v1/performance/milvus/vectors")
    public List<Long> insertVectors(int vectorCount) {
        if (vectorCount == 0) {
            return null;
        }
        List<List<Float>> vectors = generateVectors(vectorCount, MILVUS_DIMENSION);
        InsertParam insertParam = new InsertParam.Builder(MILVUS_COLLECTION_NAME).withFloatVectors(vectors).build();
        InsertResponse response = milvusClient.insert(insertParam);
        return response.getVectorIds();
    }

    @DeleteMapping("/api/v1/performance/milvus/vectors")
    public boolean delete(int vectorCount) {
        List<List<SearchResponse.QueryResult>> lists = this.search(vectorCount);
        List<Long> vectorIds = new ArrayList<>();
        lists.forEach(list -> list.forEach(queryResult -> vectorIds.add(queryResult.getVectorId())));
        Response response = milvusClient.deleteEntityByID(MILVUS_COLLECTION_NAME, "", vectorIds);
        return response.ok();
    }

    @GetMapping("/api/v1/performance/milvus/vectors")
    public List<List<SearchResponse.QueryResult>> search(int top) {
        List<List<Float>> vectorsToSearch = generateVectors(1, MILVUS_DIMENSION);
        JsonObject searchParamsJson = new JsonObject();
        searchParamsJson.addProperty("nprobe", 32);
        SearchParam searchParam = new SearchParam.Builder(MILVUS_COLLECTION_NAME).withTopK(top)
                .withParamsInJson(searchParamsJson.toString())
                .withFloatVectors(vectorsToSearch)
                .build();
        SearchResponse response = milvusClient.search(searchParam);
        if (!response.ok()) {
            throw new RuntimeException(response.getResponse().getMessage());
        }
        // log.info(new JsonMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
        return response.getQueryResultsList();
    }

    @PostMapping("/api/v1/performance/milvus/indexs")
    public boolean createIndex() {
        JsonObject indexParamsJson = new JsonObject();
        indexParamsJson.addProperty("nlist", 65536);
        Index index = new Index.Builder(MILVUS_COLLECTION_NAME, indexType)
                .withParamsInJson(indexParamsJson.toString())
                .build();
        Response response = milvusClient.createIndex(index);
        return response.ok();
    }
    @GetMapping("/api/v1/performance/milvus/indexs")
    public Index indexInfo() {
        GetIndexInfoResponse response = milvusClient.getIndexInfo(MILVUS_COLLECTION_NAME);
        return response.getIndex().orElse(null);
    }

    private List<List<Float>> generateVectors(int vectorCount, int dimension) {
        SplittableRandom splittableRandom = new SplittableRandom();
        List<List<Float>> vectors = new ArrayList<>(vectorCount);
        for (int i = 0; i < vectorCount; i++) {
            List<Float> vector = splittableRandom.split().doubles(dimension).boxed().map(Double::floatValue).collect(Collectors.toList());
            vectors.add(vector);
        }
        return vectors;
    }

}
