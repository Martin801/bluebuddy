//
//  NSMutableURLRequest+EncodedParameters.swift
//  AlamofireTest
//
//  Created by a on 04/05/17.
//  Copyright © 2017 Tuli e service. All rights reserved.
//

import Foundation
extension NSMutableURLRequest {
    
    /// Percent escape
    ///
    /// Percent escape in conformance with W3C HTML spec:
    ///
    /// See http://www.w3.org/TR/html5/forms.html#application/x-www-form-urlencoded-encoding-algorithm
    ///
    /// - parameter string:   The string to be percent escaped.
    /// - returns:            Returns percent-escaped string.
    
    private func percentEscapeString(string: String) -> String {
        let characterSet = CharacterSet(charactersIn: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._* ")
        
//        return String.stringByAddingPercentEncodingWithAllowedCharacters(characterSet)!.stringByReplacingOccurrencesOfString(" ", withString: "+", options: [], range: nil)
        
//        let str = String()
        
//        return str.stringByAddingPercentEncodingWithAllowedCharacters(characterSet)!
        
        
        return string.addingPercentEncoding(withAllowedCharacters: characterSet)!.replacingOccurrences(of: " ", with: "+")

    }
    
    /// Encode the parameters for `application/x-www-form-urlencoded` request
    ///
    /// - parameter parameters:   A dictionary of string values to be encoded in POST request
    
    func encodeParameters(parameters: [String : String]) {
        
        httpMethod = "POST"

        httpBody = parameters
            .map { "\(percentEscapeString(string: $0))=\(percentEscapeString(string: $1))" }
            .joined(separator: "&")
            .data(using: String.Encoding.utf8)
    }
}
