#include <Poco/Net/HTTPClientSession.h>
#include <Poco/Net/HTTPRequest.h>
#include <Poco/Net/HTTPResponse.h>
#include <Poco/StreamCopier.h>
#include <Poco/Path.h>
#include <Poco/URI.h>
#include <Poco/Exception.h>
#include <iostream>
#include <fstream>
#include <sstream>
#include <string>

using namespace Poco::Net;
using namespace Poco;
using namespace std; 

class ClientAPi{
public:
    ClientAPi() = default;
    ~ClientAPi() = default;
    int  Connect(const char* ServerAdress , int Port = 80);
    int  UploadImage(const char* ImagePath);
private:
    HTTPClientSession* _session{ nullptr };
    URI* _uri{ nullptr };

};

