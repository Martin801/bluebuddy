//
//  BaseParser.swift
//  Sherlock
//
//  Created by Poojashree on 5/25/17.
//  Copyright © 2017 NCR Technosolutions. All rights reserved.
//

import UIKit
import NVActivityIndicatorView

func APP_SERVICE(str: NSString) -> Any {
    //http://67.225.128.57/bluebuddyapi/api/v1/  live api
    
//    return "\("http://67.225.128.57/bluebuddyapi/ios/api/v1/")\(str)"
     return "\("http://67.225.128.57/bluebuddyapi/demo/api/v1/")\(str)"
}

class BaseParser: NSObject {
    class func callWebService(serverUrl:String,postValues:NSDictionary,methodName:String, isLoader: Bool,completionHandler:@escaping(_ status: Bool, _ finalRes:NSDictionary)-> Void){
        if Common.connectedToNetwork()
        {
            let url = URL(string:serverUrl)
            let urlRequest = NSMutableURLRequest(url: url!)
            //        urlRequest.addValue("application/json", forHTTPHeaderField: "Content-Type")
            //        urlRequest.httpMethod = "POST"
            
            urlRequest.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
            
            urlRequest.httpMethod = methodName
            urlRequest.encodeParameters(parameters: postValues as! [String : String])
//            let data1 = postValues.dataUsingEncoding(NSUTF8StringEncoding)
//            urlRequest.httpBody = data1
            let session = URLSession.shared
            print(urlRequest)
            
            if isLoader
            {
                let activityData = ActivityData()
                NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                    NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
                }
            }
            do{
                let task = session.dataTask(with: urlRequest as URLRequest){ data,response,error in
                    if isLoader
                    {
                        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                            NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
                        }
                    }
                    print("\(String(describing: data))")
                    if error != nil{
                        return
                    }
                    else{
                        
                        let jsonResponse = try? JSONSerialization.jsonObject(with: data!, options: []) as! NSDictionary
                        print(jsonResponse!)
                        if(jsonResponse != nil){
                            
                            if let response = response as? HTTPURLResponse, 200...299 ~= response.statusCode
                            {
                                completionHandler(true, jsonResponse! )
                            }
                            else
                            {
                                completionHandler(false, jsonResponse!)
                            }
                        }
                        else {
                            return
                        }
                    }
                }
                task.resume()
            }

        }
        else{
            Common.showAlertForInternet(viewController: (APP_DELEGATE.window?.currentViewController)!)
        }
    }
    class func callWebServiceWithHeaderAndLoader(serverUrl:String, postValues:NSDictionary, methodName:String, isLoader:Bool ,completionHandler:@escaping(_ status: Bool, _ finalRes:NSDictionary)-> Void){
        
        let url = URL(string:serverUrl)
        let urlRequest = NSMutableURLRequest(url: url!)
        //        urlRequest.addValue("application/json", forHTTPHeaderField: "Content-Type")
        //        urlRequest.httpMethod = "POST"
        
        urlRequest.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        let strToken = UserDefaults.standard.value(forKey: kUserToken) as! String
        urlRequest.addValue("Bearer" + " " + strToken, forHTTPHeaderField: "Authorizations")
        
        urlRequest.httpMethod = methodName
        if postValues.count == 0
        {
            
        }
        else
        {
            urlRequest.encodeParameters(parameters: postValues as! [String : String])
        }
        let session = URLSession.shared
        
        let activityData = ActivityData()
        NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
            NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
        }
        do{
            print("URL::",url as Any)
            let task = session.dataTask(with: urlRequest as URLRequest){ data,response,error in
                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                    NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
                }
                let httpResponse = response as? HTTPURLResponse
                if (httpResponse != nil) {
                    print("statusCode: \(httpResponse?.statusCode ?? 200)")
                }
                print("\(String(describing: data))")
                
                if error != nil{
                    return
                }
                else if (httpResponse != nil) && httpResponse?.statusCode == 401 {
                    print("statusCode: \(httpResponse?.statusCode ?? 200)")
                }
                else{
                    let jsonResponse = try? JSONSerialization.jsonObject(with: data!, options: []) as! NSDictionary
                    print(jsonResponse!)
                    if(jsonResponse != nil){
                        
                        if let response = response as? HTTPURLResponse, 200...299 ~= response.statusCode
                        {
                            completionHandler(true, jsonResponse! )
                        }
                        else {
                            completionHandler(false, jsonResponse!)
                        }
                    }
                    else {
                        return
                    }
                }
            }
            task.resume()
        }
    }

    class func callWebServiceWithHeader(serverUrl:String,postValues:NSDictionary,methodName:String,completionHandler:@escaping(_ status: Bool, _ finalRes:NSDictionary)-> Void){
        BaseParser.callWebServiceWithHeaderAndLoader(serverUrl: serverUrl, postValues: postValues, methodName: methodName, isLoader: false) { (status, strDict) in
            DispatchQueue.main.async {
                completionHandler(status, strDict)
            }
        }
    }
    class func callWebServiceWithHeaderForGet(serverUrl:String, postValues:NSDictionary, methodName:String, isLoader:Bool ,completionHandler:@escaping(_ status: Bool, _ finalRes:NSDictionary)-> Void){
        
        var components = URLComponents(string: serverUrl)!
        
        components.queryItems = postValues.map { (key, value) in
            URLQueryItem(name: key as! String, value: value as? String)
        }
        
        components.percentEncodedQuery = components.percentEncodedQuery?.replacingOccurrences(of: "+", with: "%2B")
        let urlRequest = NSMutableURLRequest(url: components.url!)
        //        urlRequest.addValue("application/json", forHTTPHeaderField: "Content-Type")
        //        urlRequest.httpMethod = "POST"
        
        urlRequest.addValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        let strToken = UserDefaults.standard.value(forKey: kUserToken) as! String
        urlRequest.addValue("Bearer" + " " + strToken, forHTTPHeaderField: "Authorizations")
        
        urlRequest.httpMethod = methodName
        let session = URLSession.shared
        if isLoader
        {
            let activityData = ActivityData()
            NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
            }
        }
        do{
            let task = session.dataTask(with: urlRequest as URLRequest){ data,response,error in
                if isLoader
                {
                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                        NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
                    }
                }
                let httpResponse = response as? HTTPURLResponse
                if (httpResponse != nil) {
                    print("statusCode: \(httpResponse?.statusCode ?? 200)")
                }
                print("\(String(describing: data))")
                
                if error != nil{
                    return
                }
                else if (httpResponse != nil) && httpResponse?.statusCode == 401 {
                    print("statusCode: \(httpResponse?.statusCode ?? 200)")
                }
                else{
                    let jsonResponse = try? JSONSerialization.jsonObject(with: data!, options: []) as! NSDictionary
                    print(jsonResponse!)
                    if(jsonResponse != nil){
                        
                        if let response = response as? HTTPURLResponse, 200...299 ~= response.statusCode
                        {
                            completionHandler(true, jsonResponse! )
                        }
                        else {
                            completionHandler(false, jsonResponse!)
                        }
                    }
                    else {
                        return
                    }
                }
            }
            task.resume()
        }
    }



    class func callMultiPartDataWebService(serverUrl:String,postValues:NSDictionary,methodName:String,completionHandler:@escaping(_ status: Bool, _ finalRes:NSDictionary)-> Void){
        
        var request  = URLRequest(url: URL(string: serverUrl)!)
        request.httpMethod = "POST"
        let boundary1 = "Boundary-\(NSUUID().uuidString)"
        request.setValue("multipart/form-data; boundary=\(boundary1)", forHTTPHeaderField: "Content-Type")
        let strToken = UserDefaults.standard.value(forKey: kUserToken) as! String
        request.addValue("Bearer" + " " + strToken, forHTTPHeaderField: "Authorizations")
        let activityData = ActivityData()
        NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
            NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
        }

        request.httpBody = createBodyWithParameters(parameters: postValues, boundary: boundary1) as Data
        let session = URLSession.shared
        
        
        do{
            let task = session.dataTask(with: request as URLRequest){ data,response,error in
                if let httpResponse = response as? HTTPURLResponse {
                    print("statusCode: \(httpResponse.statusCode)")
                }
                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                    NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
                }
                print("\(String(describing: data))")
                if error != nil{
                    return
                    
                }else{
                    let jsonResponse = try? JSONSerialization.jsonObject(with: data!, options: []) as! NSDictionary
                    print(jsonResponse!)
                    if(jsonResponse != nil){
                        
                        if let response = response as? HTTPURLResponse, 200...299 ~= response.statusCode
                        {
                            completionHandler(true, jsonResponse! )
                        }
                        else {
                            completionHandler(false, jsonResponse!)
                        }
                    }
                    else {
                        return
                    }
                }
            }
            task.resume()
        }
    }
    class func createBodyWithParameters(parameters: NSDictionary?,boundary: String) -> NSData {
        let body = NSMutableData()
        
        if parameters != nil {
            for (key, value) in parameters! {
                
                if(value is String || value is NSString){
                    body.appendString("--\(boundary)\r\n")
                    body.appendString("Content-Disposition: form-data; name=\"\(key)\"\r\n\r\n")
                    body.appendString("\(value)\r\n")
                }
                else if(value is UIImage){
//                    for image in value as! UIImage{
//                        if(value == UIImage()){
                            //image is nil, or has no data
//                            let data = UIImagePNGRepresentation(value as! UIImage)
                            let data = UIImageJPEGRepresentation(value as! UIImage, 0.5)
                            let filename = "image.jpg"
                            let mimetype = "image/jpg"
                            
                            body.appendString("--\(boundary)\r\n")
                            body.appendString("Content-Disposition: form-data; name=\"\(key)\"; filename=\"\(filename)\"\r\n")
                            body.appendString("Content-Type: \(mimetype)\r\n\r\n")
                            body.append(data!)

                            body.appendString("\r\n")
//                        }
//                    }
                }
                body.appendString("--\(boundary)--\r\n")

            }
        }
        
        // NSLog("data %@",NSString(data: body as Data, encoding: String.Encoding.utf8.rawValue)!);
        return body
    }

//    class func createBodyWithParameters(parameters: NSDictionary?,boundary: String) -> NSData {
//        let body = NSMutableData()
//        
//        if parameters != nil {
//            for (key, value) in parameters! {
//                
//                if(value is String || value is NSString){
//                    body.appendString("--\(boundary)\r\n")
//                    body.appendString("Content-Disposition: form-data; name=\"\(key)\"\r\n\r\n")
//                    body.appendString("\(value)\r\n")
//                }
//                else if(value is [UIImage]){
//                    var i = 0;
//                    for image in value as! [UIImage]{
//                        if(image == nil || image == UIImage()){
//                            //image is nil, or has no data
//                            let data = UIImagePNGRepresentation(image)
//                            let filename = "image\(i+1).jpg"
//                            let mimetype = "image/jpg"
//                            
//                            body.appendString("--\(boundary)\r\n")
//                            body.appendString("Content-Disposition: form-data; name=\"\(key)[\(i)]\"; filename=\"\(filename)\"\r\n")
//                            body.appendString("Content-Type: \(mimetype)\r\n\r\n")
//                            body.append(data!)
//                            body.appendString("\r\n")
//                            i += 1;
//                        }
//                    }
//                }
//            }
//        }
//        body.appendString("--\(boundary)--\r\n")
//        // NSLog("data %@",NSString(data: body as Data, encoding: String.Encoding.utf8.rawValue)!);
//        return body
//    }
    
    
    func queryItems(dictionary: [String:Any]) -> String {
        var components = URLComponents()
        print(components.url!)
        components.queryItems = dictionary.map {
            URLQueryItem(name: $0, value: $1  as? String)
        }
        return (components.url?.absoluteString)!
    }
}
extension NSMutableData {
    func appendString(_ string: String) {
        let data = string.data(using: String.Encoding.utf8, allowLossyConversion: false)
        append(data!)
    }
}

