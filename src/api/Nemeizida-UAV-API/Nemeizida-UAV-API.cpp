#include <iostream>
#include "ClientAPi.h"

int main(){
	ClientAPi _Api{};
	if (_Api.Connect("http://b95b8d4ad602.ngrok.io")){
		printf("Connected to server");
        _Api.UploadImage("ing5.JPG");
	}
	else{
		printf("Fail connect to server");
	}
 
}



#if 0
int main(int argc, char** argv)
{
    if (argc != 2)
    {
        cout << "Usage: " << argv[0] << " <uri>" << endl;
        cout << "       fetches the resource identified by <uri> and print it" << endl;
        return -1;
    }

    try
    {
        // prepare session
        URI uri(argv[1]);
        HTTPClientSession session(uri.getHost(), uri.getPort());
        
        // prepare path
        string path(uri.getPathAndQuery());
        if (path.empty()) path = "/";

        // send request
        HTTPRequest req(HTTPRequest::HTTP_GET, path, HTTPMessage::HTTP_1_1);
        session.sendRequest(req);

        // get response
        HTTPResponse res;
        cout << res.getStatus() << " " << res.getReason() << endl;

        // print response
        istream& is = session.receiveResponse(res);
        StreamCopier::copyStream(is, cout);
    }
    catch (Exception& ex)
    {
        cerr << ex.displayText() << endl;
        return -1;
    }

    return 0;
}
#endif // 0