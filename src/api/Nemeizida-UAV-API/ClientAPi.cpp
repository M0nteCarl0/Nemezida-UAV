#include "ClientAPi.h"

int ClientAPi::Connect(const char* ServerAdress, int Port){
    if (ServerAdress == nullptr){
        return 0;
    }
    int Flag = 0;
    if (_uri == nullptr) {
        _uri = new URI{};
    };
    _uri->setHost( ServerAdress );
    _uri->setPort( Port) ;
    if (_session == nullptr) {
        _session = new HTTPClientSession{ _uri->getHost(), _uri->getPort() };
    }
    std::string path{ _uri->getPathAndQuery() };
    if (path.empty()) path = "/";
    HTTPRequest req{ HTTPRequest::HTTP_GET, path, HTTPMessage::HTTP_1_1 };
    HTTPResponse res;
    if (res.getStatus() == HTTPResponse::HTTPStatus::HTTP_OK){
        Flag = 1;
    }
    return Flag;
}

int ClientAPi::UploadImage(const char* ImagePath){
    int Flag = 0;
    std::string path{ _uri->getPathAndQuery() };
    std::string reqBody;
    if (path.empty()) path = "/api/uav/MARK_IMG";
    HTTPRequest req{ HTTPRequest::HTTP_POST, path, HTTPMessage::HTTP_1_1 };
    reqBody.append("image");
    req.setContentType("multipart/form-data");
    req.setKeepAlive(true);
    req.setContentLength(reqBody.length());
    std::ifstream file{ ImagePath, std::ios::binary };
    std::ostringstream ostrm;
    ostrm << file.rdbuf();
    std::ostream& myOStream = _session->sendRequest(req);
    myOStream << reqBody;
    HTTPResponse res;
    cout << res.getStatus() << " " << res.getReason() << endl;
    if (res.getStatus() == HTTPResponse::HTTPStatus::HTTP_OK) {
        Flag = 1;
    }
    return Flag;
}
