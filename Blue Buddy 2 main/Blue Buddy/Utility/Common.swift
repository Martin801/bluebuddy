//
//  Common.swift
//  LEA
//
//  Created by Poojashree on 9/8/17.
//  Copyright © 2017 NCR Technosolutions. All rights reserved.
//

import UIKit
import SystemConfiguration
import SKPhotoBrowser
import CoreLocation

let baseURL : String = ""

//var config = SwiftLoader.Config()

let strDeviceID : String = (UIDevice.current.identifierForVendor?.uuidString)!

let kUserRequestKey = "User Request Key"

let kUserEmail = "UserEmail"
let kUserPass = "UserPass"
let kisLogin = "kisLogin"
let kisFBLogin = "kisFBLogin"
let kFBID = "kFBID"
let kUserID = "UserID"
let kUserToken = "UserToken"
let kUserDeviceToken = "kUserDeviceToken"
let kPageNo = "PageNo"
let imgUrl = "http://67.225.128.57/bluebuddyapi/demo/uploads/"
class Common: NSObject {

    
    //MARK:- Show Alert View
    
    class func showAlert (message:String?, title:String?,viewController:UIViewController){
        
        let alert = UIAlertController(title: title,
                                      message: message,
                                      preferredStyle: UIAlertControllerStyle.alert)
        
        let cancelAction = UIAlertAction(title: "OK",
                                         style: .cancel, handler: nil)
        
        alert.addAction(cancelAction)
        viewController.present(alert, animated: true, completion: nil)
        
    }
    
    
    //MARK:- Check Internet Connection
    
    
    
    class func connectedToNetwork() -> Bool {
        
        var zeroAddress = sockaddr_in()
        zeroAddress.sin_len = UInt8(MemoryLayout.size(ofValue: zeroAddress))
        zeroAddress.sin_family = sa_family_t(AF_INET)
        
        
        let defaultRouteReachability = withUnsafePointer(to: &zeroAddress) {
            $0.withMemoryRebound(to: sockaddr.self, capacity: 1) {zeroSockAddress in
                SCNetworkReachabilityCreateWithAddress(nil, zeroSockAddress)
            }
        }
        
        var flags : SCNetworkReachabilityFlags = []
        if !SCNetworkReachabilityGetFlags(defaultRouteReachability!, &flags) {
            return false
        }
        
        let isReachable = flags.contains(.reachable)
        let needsConnection = flags.contains(.connectionRequired)
        
        return (isReachable && !needsConnection)
    }

    class func isValidPhone(testStr:String) -> Bool
    {
//    let phoneRegex = "^((\\+)|(00))[0-9]{6,14}$" as String
    let phoneRegex = "^((\\+)|(00))[0-9]{6,14}$" as String
    let phoneTest = NSPredicate(format:"SELF MATCHES %@", phoneRegex)
    return phoneTest.evaluate(with: testStr)
    }
    
    class func showAlertForInternet (viewController:UIViewController){
        
        let alert = UIAlertController(title: "BLUE BUDDY",
                                      message: "Please check your internet connection and make sure your location is enabled from settings.",
                                      preferredStyle: UIAlertControllerStyle.alert)
        let settingAction = UIAlertAction(title: NSLocalizedString("SETTING", comment: "setting action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
            
            guard let settingsUrl = URL(string: UIApplicationOpenSettingsURLString) else {
                return
            }
            
            if UIApplication.shared.canOpenURL(settingsUrl) {
                UIApplication.shared.open(settingsUrl, completionHandler: { (success) in
                    print("Settings opened: \(success)") // Prints true
                })
            }
        })
        let cancelAction = UIAlertAction(title: "CANCEL", style: .cancel, handler: nil)
        
        alert.addAction(settingAction)
        alert.addAction(cancelAction)
        viewController.present(alert, animated: true, completion: nil)
    }
    
    class func displayImageLarger(viewController:UIViewController, displayImageUrl:URL) {
        
        SKPhotoBrowserOptions.displayToolbar = true
        SKPhotoBrowserOptions.displayBackAndForwardButton = true
        SKPhotoBrowserOptions.displayAction = false
        var images = [SKPhotoProtocol]()
        
        let photo = SKPhoto.photoWithImageURL(displayImageUrl.absoluteString)
        photo.shouldCachePhotoURLImage = false // add some UIImage
        images.append(photo)
        
        
        let browser = SKPhotoBrowser(photos: images)
        browser.initializePageIndex(0)
        viewController.present(browser, animated: true, completion: {})
        
    }
    
    class func displayImageLargerFromArray(viewController:UIViewController, arrImages:NSArray, andIndex:Int) {
        
        SKPhotoBrowserOptions.displayToolbar = true
        SKPhotoBrowserOptions.displayBackAndForwardButton = true
        SKPhotoBrowserOptions.displayAction = false
        var images = [SKPhotoProtocol]()
        
        for imgData in arrImages {
            let imgData1 = imgData as! ImageBO
            let photo = SKPhoto.photoWithImageURL(imgData1.image_name)
            photo.shouldCachePhotoURLImage = false // add some UIImage
            images.append(photo)
        }
        
        
        let browser = SKPhotoBrowser(photos: images)
        browser.initializePageIndex(andIndex)
        viewController.present(browser, animated: true, completion: {})
        
    }
    

    class func getAddressFromCurrentLatLong( completion: @escaping (String) -> ()) {
        
        var strAddress = String()
        strAddress = ""
        let objLocation = CLLocation(latitude: APP_DELEGATE.currLoc.coordinate.latitude, longitude: APP_DELEGATE.currLoc.coordinate.longitude)
        CLGeocoder().reverseGeocodeLocation(objLocation) { (placemarksArray, error) in
            if error != nil {
                print("Reverse geocoder failed with error" + (error?.localizedDescription)!)
                completion("")
                return
            }
            
            if (placemarksArray?.count)! > 0 {
                let objPlacemark = placemarksArray?[0]
                
                print("objPlacemark : \(objPlacemark?.description)")
                strAddress = (objPlacemark?.name)! as String
                completion(strAddress)
            }
            else {
                
                print("Problem with the data received from geocoder")
                completion("")
            }
        }
        
    }
    
}


extension UIDevice {
    var iPhoneX: Bool {
        return UIScreen.main.nativeBounds.height == 2436
    }
    var iPhone: Bool {
        return UIDevice.current.userInterfaceIdiom == .phone
    }
    enum ScreenType: String {
        case iPhone4_4S = "iPhone 4 or iPhone 4S"
        case iPhones_5_5s_5c_SE = "iPhone 5, iPhone 5s, iPhone 5c or iPhone SE"
        case iPhones_6_6s_7_8 = "iPhone 6, iPhone 6S, iPhone 7 or iPhone 8"
        case iPhones_6Plus_6sPlus_7Plus_8Plus = "iPhone 6 Plus, iPhone 6S Plus, iPhone 7 Plus or iPhone 8 Plus"
        case iPhoneX = "iPhone X"
        case unknown
    }
    var screenType: ScreenType {
        switch UIScreen.main.nativeBounds.height {
        case 960:
            return .iPhone4_4S
        case 1136:
            return .iPhones_5_5s_5c_SE
        case 1334:
            return .iPhones_6_6s_7_8
        case 1920, 2208:
            return .iPhones_6Plus_6sPlus_7Plus_8Plus
        case 2436:
            return .iPhoneX
        default:
            return .unknown
        }
    }
}
