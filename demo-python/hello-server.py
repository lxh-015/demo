import SimpleHTTPServer
import SocketServer

PORT = 8080

class myHandler(SimpleHTTPServer.SimpleHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
    self.send_header('Content-type','text/html')
    self.end_headers()
    # Send the html message
    self.wfile.write("Hello World !")

try:
    httpd = SocketServer.TCPServer(("", PORT), myHandler)
    print('Started httpserver on port: ', PORT)
    httpd.serve_forever()
except KeyboardInterrupt:
    print('^C received, shutting down the web server')
    httpd.socket.close()
