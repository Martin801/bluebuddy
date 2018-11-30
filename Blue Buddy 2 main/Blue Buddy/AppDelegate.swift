//
//  AppDelegate.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/1/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlaces
import GoogleMaps
import IQKeyboardManager
import NVActivityIndicatorView
import Firebase
import FBSDKCoreKit
import FBSDKLoginKit
import CoreLocation
import UserNotifications

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, CLLocationManagerDelegate, UNUserNotificationCenterDelegate, MessagingDelegate{

    var window: UIWindow?
    var navController: UINavigationController?
    var stryBoard: UIStoryboard?
    var dateFormatter = DateFormatter()
    let locationManager = CLLocationManager()
    var currLoc = CLLocation()
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        FirebaseApp.configure()
        FBSDKLoginManager.initialize()
        UNUserNotificationCenter.current().delegate = self
        
        let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
        UNUserNotificationCenter.current().requestAuthorization(
            options: authOptions,
            completionHandler: {_, _ in })
        application.registerForRemoteNotifications()
        Messaging.messaging().delegate = self
        FBSDKApplicationDelegate.sharedInstance().application(application, didFinishLaunchingWithOptions: launchOptions)
        window = UIWindow(frame: UIScreen.main.bounds)
        stryBoard = UIStoryboard(name: "Main", bundle: nil)
        let loginVC = stryBoard?.instantiateViewController(withIdentifier: "LoginViewController")
        navController =  UINavigationController(rootViewController: loginVC!)
        navController?.navigationBar.isHidden = true
        window?.rootViewController = navController
        UserDefaults.standard.set(false, forKey: "isProfileOpen")
        GMSServices.provideAPIKey("AIzaSyANNYFHakO4sT1EuumxXxmwD-DhAbdX5bk")
        GMSPlacesClient.provideAPIKey("AIzaSyANNYFHakO4sT1EuumxXxmwD-DhAbdX5bk")
        NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
        locationManager.requestAlwaysAuthorization()
        if CLLocationManager.locationServicesEnabled() {
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest // You can change the locaiton accuary here.
            locationManager.startUpdatingLocation()
        }
        IQKeyboardManager.shared().isEnabled = true
        
        var aView: UIView?
        let viewController: UIViewController? = UIStoryboard(name: "LaunchScreen", bundle: nil).instantiateViewController(withIdentifier: "viewcontroller")
        aView = viewController?.view
        aView?.frame = (window?.bounds)!
        navController?.view.addSubview(aView!)
        
        DispatchQueue.main.asyncAfter(deadline: .now()) {
            let isfbLogin = UserDefaults.standard.bool(forKey: kisFBLogin)
            if !isfbLogin {
                let isLogin = UserDefaults.standard.bool(forKey: kisLogin)
                if isLogin{
                    if Common.connectedToNetwork()
                    {
                        let strUserEmail = UserDefaults.standard.value(forKey: kUserEmail)
                        let strUserPass = UserDefaults.standard.value(forKey: kUserPass)
                        
                        let dictVal = NSMutableDictionary()
                        dictVal["email"] = strUserEmail
                        dictVal["password"] = strUserPass
                        dictVal["firebase_api_key"] = UserDefaults.standard.value(forKey: kUserDeviceToken)
                        dictVal["device_type"] = "ios"
                        UserParser.callLoginUserService(dictVal, isLoader: false, complete: { (status, strMsg, strUserStatus, dictResult) in
                            if status{
                                let stepNo = UserDefaults.standard.value(forKey: kPageNo) as? String
                                UserDefaults.standard.set(strUserEmail, forKey: kUserEmail)
                                UserDefaults.standard.set(strUserPass, forKey: kUserPass)
                                UserDefaults.standard.set(true, forKey: kisLogin)
                                DispatchQueue.main.async {
                                    if stepNo == "1"
                                    {
                                        self.animateRemoveLaunchScreen(aView!)
                                        let profileVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CreateProfileViewController") as! CreateProfileViewController
                                        self.navController?.pushViewController(profileVC, animated: true)
                                        
                                    }
                                    else if (stepNo == "2")
                                    {
                                        self.animateRemoveLaunchScreen(aView!)
                                        
                                        let certificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationViewController") as! CertificationViewController
                                        self.navController?.pushViewController(certificationVC, animated: true)
                                    }
                                    else if (stepNo == "11")
                                    {
                                        self.animateRemoveLaunchScreen(aView!)
                                        
                                        let certificationDetailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationDetailsViewController") as? CertificationDetailsViewController
                                        certificationDetailsVC?.strSelectedItem = 1
                                        self.navController?.pushViewController(certificationDetailsVC!, animated: true)
                                    }
                                    else if (stepNo == "12")
                                    {
                                        self.animateRemoveLaunchScreen(aView!)
                                        
                                        let freeDiverVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "FreeDiverCertificationDetailsViewController") as! FreeDiverCertificationDetailsViewController
                                        self.navController?.pushViewController(freeDiverVC, animated: true)
                                    }
                                    else if (stepNo == "4")
                                    {
                                        self.animateRemoveLaunchScreen(aView!)
                                        
                                        let aboutVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AboutYourselfViewController") as! AboutYourselfViewController
                                        self.navController?.pushViewController(aboutVC, animated: true)
                                    }
                                    else if (stepNo == "5")
                                    {
                                        self.animateRemoveLaunchScreen(aView!)
                                        
                                        let socialVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddSocialLinkViewController") as! AddSocialLinkViewController
                                        self.navController?.pushViewController(socialVC, animated: true)
                                    }
                                    else if (stepNo == "6")
                                    {
                                        self.animateRemoveLaunchScreen(aView!)
                                        
                                        let aboutVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "OtherLinkViewController") as! OtherLinkViewController
                                        self.navController?.pushViewController(aboutVC, animated: true)
                                    }
                                    else if (stepNo == "7")
                                    {
                                        self.animateRemoveLaunchScreen(aView!)
                                        
                                        let locationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "LocationViewController") as! LocationViewController
                                        self.navController?.pushViewController(locationVC, animated: true)
                                    }
                                    else if (stepNo == "8")
                                    {
                                        self.animateRemoveLaunchScreen(aView!)
                                        
                                        let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "UploadPicViewController") as! UploadPicViewController
                                        self.navController?.pushViewController(uploadVC, animated: true)
                                    }
                                    else if (stepNo == "9")
                                    {
                                        UserParser.callProfileDetailsService("", complete: { (status, strMsg, objProfile) in
                                            if status
                                            {
                                                DispatchQueue.main.async {
                                                    let newViewController = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "TabViewController") as! TabViewController
                                                    newViewController.selectedIndex = 4
                                                    self.navController?.present(newViewController, animated: false, completion: nil)
                                                    DispatchQueue.main.asyncAfter(deadline: .now())
                                                    {
                                                        self.animateRemoveLaunchScreen(aView!)
                                                    }
                                                }
                                            }
                                        })
                                    }
                                }
                            }
                            else if strUserStatus == "0"
                            {
                                DispatchQueue.main.async {
                                    let otpVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "OTPVerificationViewController") as? OTPVerificationViewController
                                    otpVC?.strOpt = ""
                                    otpVC?.isPopUpOpen = true
                                    self.navController?.pushViewController(otpVC!, animated: true)
                                    self.animateRemoveLaunchScreen(aView!)
                                }
                            }
                            else{
                                DispatchQueue.main.async {
                                    //                            Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self.window)
                                    self.animateRemoveLaunchScreen(aView!)
                                }
                            }
                        })
                    }
                    else
                    {
                        self.animateRemoveLaunchScreen(aView!)
                        Common.showAlertForInternet(viewController: (self.window?.currentViewController)!)
                    }
                }
                else
                {
                    self.animateRemoveLaunchScreen(aView!)
                }
            }
            else {
//                UserParser.callProfileDetailsService("", complete: { (status, strMsg, objProfile) in
//                    if status
//                    {
//                        DispatchQueue.main.async {
//                            let newViewController = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "TabViewController") as! TabViewController
//                            newViewController.selectedIndex = 4
//                            self.navController?.present(newViewController, animated: false, completion: nil)
//                            DispatchQueue.main.asyncAfter(deadline: .now())
//                            {
//                                self.animateRemoveLaunchScreen(aView!)
//                            }
//                        }
//                    }
//                })
                
                let dictVal = NSMutableDictionary()
                dictVal["social_id"] = UserDefaults.standard.value(forKey: kFBID)
                dictVal["social"] = "facebook"
                dictVal["firebase_api_key"]    = UserDefaults.standard.value(forKey: kUserDeviceToken)
                dictVal["device_type"] = "ios"
                UserParser.callLoginWithFacebook(dictVal, isLoader: true, complete: { (status, strMsg, new_socialuser) in
                    DispatchQueue.main.async {
                        if new_socialuser
                        {
                            
                        }
                        else if status{
                            UserDefaults.standard.set(true, forKey: kisFBLogin)
                            let stepNo = UserDefaults.standard.value(forKey: kPageNo) as? String
                            DispatchQueue.main.async {
                                if (stepNo == "2")
                                {
                                    self.animateRemoveLaunchScreen(aView!)
                                    
                                    let certificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationViewController") as! CertificationViewController
                                    self.navController?.pushViewController(certificationVC, animated: true)
                                }
                                else if (stepNo == "11")
                                {
                                    self.animateRemoveLaunchScreen(aView!)
                                    
                                    let certificationDetailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationDetailsViewController") as? CertificationDetailsViewController
                                    certificationDetailsVC?.strSelectedItem = 1
                                    self.navController?.pushViewController(certificationDetailsVC!, animated: true)
                                }
                                else if (stepNo == "12")
                                {
                                    self.animateRemoveLaunchScreen(aView!)
                                    
                                    let freeDiverVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "FreeDiverCertificationDetailsViewController") as! FreeDiverCertificationDetailsViewController
                                    self.navController?.pushViewController(freeDiverVC, animated: true)
                                }
                                else if (stepNo == "4")
                                {
                                    self.animateRemoveLaunchScreen(aView!)
                                    
                                    let aboutVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AboutYourselfViewController") as! AboutYourselfViewController
                                    self.navController?.pushViewController(aboutVC, animated: true)
                                }
                                else if (stepNo == "5")
                                {
                                    self.animateRemoveLaunchScreen(aView!)
                                    
                                    let socialVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddSocialLinkViewController") as! AddSocialLinkViewController
                                    self.navController?.pushViewController(socialVC, animated: true)
                                }
                                else if (stepNo == "6")
                                {
                                    self.animateRemoveLaunchScreen(aView!)
                                    
                                    let aboutVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "OtherLinkViewController") as! OtherLinkViewController
                                    self.navController?.pushViewController(aboutVC, animated: true)
                                }
                                else if (stepNo == "7")
                                {
                                    self.animateRemoveLaunchScreen(aView!)
                                    
                                    let locationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "LocationViewController") as! LocationViewController
                                    self.navController?.pushViewController(locationVC, animated: true)
                                }
                                else if (stepNo == "8")
                                {
                                    self.animateRemoveLaunchScreen(aView!)
                                    
                                    let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "UploadPicViewController") as! UploadPicViewController
                                    self.navController?.pushViewController(uploadVC, animated: true)
                                }
                                else if (stepNo == "9")
                                {
                                    UserParser.callProfileDetailsService("", complete: { (status, strMsg, objProfile) in
                                        if status
                                        {
                                            DispatchQueue.main.async {
                                                let newViewController = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "TabViewController") as! TabViewController
                                                newViewController.selectedIndex = 4
                                                self.navController?.present(newViewController, animated: false, completion: nil)
                                                DispatchQueue.main.asyncAfter(deadline: .now())
                                                {
                                                    self.animateRemoveLaunchScreen(aView!)
                                                }
                                            }
                                        }
                                    })
                                }
                            }
                        }
                        else{
                            Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: (self.window?.currentViewController)!)
                        }
                    }
                })
                

            }
            
            
        }
        // Override point for customization after application launch.
        return true
    }

    
    func animateRemoveLaunchScreen(_ aView: UIView) {
        UIView.animate(withDuration: 0.3, animations: {() -> Void in
            aView.alpha = 0.01
            aView.transform = CGAffineTransform(scaleX: 0.7, y: 0.7)
        }, completion: {(_ finished: Bool) -> Void in
            aView.removeFromSuperview()
        })
    }
    
    
    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        FBSDKAppEvents.activateApp()
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    func application(_ application: UIApplication, open url: URL, sourceApplication: String?, annotation: Any) -> Bool {
        return FBSDKApplicationDelegate.sharedInstance().application(application, open: url, sourceApplication: sourceApplication, annotation: annotation)
    }
    
    func changeOneFormateToOther( _ firstFormate: String, _ finalFormate: String, _ strDate: String) -> String
    {
        dateFormatter.dateFormat = firstFormate
        let dateVal: Date = (dateFormatter.date(from: strDate))!
        dateFormatter.dateFormat = finalFormate
        let finalDateStr:String = dateFormatter.string(from: dateVal)
        return finalDateStr
    }
    func changeDateToString( _ finalFormate: String, _ date: Date) -> NSString
    {
        dateFormatter.dateFormat = finalFormate
        let value = dateFormatter.string(from: date)
        return value as NSString
    }
    
    func heightWithConstrainedWidth(width: CGFloat, font: UIFont, string: String) -> CGFloat {
        let constraintRect = CGSize(width: width, height: 5000)
//        let constraintRect = CGSize(width: width, height: .greatestFiniteMagnitude)//DBL_MAX
        let boundingBox = string.boundingRect(with: constraintRect, options: [.usesLineFragmentOrigin, .usesFontLeading], attributes: [NSFontAttributeName: font], context: nil)
        return boundingBox.height
    }
    
    // MARK: -
    // MARK: - CLLocationManager Delegate Methods
    // Print out the location to the console
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.first {
            print(location.coordinate)
            currLoc = location
        }
    }
    
    // If we have been deined access give the user the option to change it
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if(status == CLAuthorizationStatus.denied) {
           // showLocationDisabledPopUp()
        }
    }
    
    // Show the popup to the user if we have been deined access
//    func showLocationDisabledPopUp() {
//        let alertController = UIAlertController(title: "Background Location Access Disabled",
//                                                message: "In order to deliver pizza we need your location",
//                                                preferredStyle: .alert)
//
//        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
//        alertController.addAction(cancelAction)
//
//        let openAction = UIAlertAction(title: "Open Settings", style: .default) { (action) in
//            if let url = URL(string: UIApplicationOpenSettingsURLString) {
//                UIApplication.shared.open(url, options: [:], completionHandler: nil)
//            }
//        }
//        alertController.addAction(openAction)
//
//        self.present(alertController, animated: true, completion: nil)
//    }
    // MARK: -
    // MARK: - UNNotification Methods
    var applicationStateString: String {
        if UIApplication.shared.applicationState == .active {
            return "active"
        } else if UIApplication.shared.applicationState == .background {
            return "background"
        }else {
            return "inactive"
        }
    }
    func messaging(_ messaging: Messaging, didRefreshRegistrationToken fcmToken: String) {
        NSLog("[RemoteNotification] didRefreshRegistrationToken: \(fcmToken)")
        UserDefaults.standard.set(fcmToken, forKey:kUserDeviceToken)
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        let userInfo = notification.request.content.userInfo as NSDictionary
        let stringDetails = userInfo["push_type"] as! String
        let strMsg = userInfo["message"] as! String
        //   let val = convertToDictionary(text: stringDetails)
        // let val = dictDetails.value(forKey: "push_type")
        let push_type = Int(stringDetails)
        if applicationStateString == "active"
        {
            if push_type == 1
            {
                if (self.navController?.visibleViewController?.isEqual(ChatViewController.self))!
                {
                    
                }
            }
            else if (push_type == 2)
            {
                self.callalertForPush(push_type!, strMsg)
            }
            else if (push_type == 3)
            {
                self.callalertForPush(push_type!, strMsg)
            }
            else if (push_type == 4)
            {
                self.callalertForPush(push_type!, strMsg)
            }
            else if (push_type == 5)
            {
                DispatchQueue.main.asyncAfter(deadline: .now() + 4.0, execute: {
                    let alert = UIAlertController(title: "BLUE BUDDY",
                                                  message: strMsg,
                                                  preferredStyle: UIAlertControllerStyle.alert)
                    
                    let cancelAction = UIAlertAction(title: "Ok",
                                                     style: .cancel, handler: nil)
                    
                    alert.addAction(cancelAction)
                    let activeViewCont = self.navController?.visibleViewController
                    activeViewCont?.present(alert, animated: true, completion: nil)
                })
                
               
            }
            else if (push_type == 8)
            {
                let alert = UIAlertController(title: "BLUE BUDDY",
                                              message: strMsg,
                                              preferredStyle: UIAlertControllerStyle.alert)
                
                let cancelAction = UIAlertAction(title: "Ok",
                                                 style: .cancel, handler: nil)
                
                alert.addAction(cancelAction)
                let activeViewCont = self.navController?.visibleViewController
                activeViewCont?.present(alert, animated: true, completion: nil)
            }
            else{
                
            }
        }
        else{
            if push_type == 1
            {
                
            }
            else if (push_type == 2)
            {
                
            }
            else if (push_type == 3)
            {
                
            }
            else if (push_type == 4)
            {
                
            }
            else{
                
            }
        }
//        switch push_type {
//        case 1: break
//
//        default:
//            break
//        }
        NSLog("[UserNotificationCenter] applicationState: \(applicationStateString) willPresentNotification: \(userInfo)")
        //TODO: Hadle foreground notification
        completionHandler([.alert])
    }
    
    // iOS10+, called when received response (default open, dismiss or custom action) for a notification
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        let userInfo = response.notification.request.content.userInfo as NSDictionary
        NSLog("[UserNotificationCenter] applicationState: \(applicationStateString) didReceiveResponse: \(userInfo)")
        let stringDetails = userInfo["push_type"] as! String
        let strMsg = userInfo["message"] as! String
        //   let val = convertToDictionary(text: stringDetails)
        // let val = dictDetails.value(forKey: "push_type")
        let push_type = Int(stringDetails)
        if applicationStateString == "active"
        {
            if push_type == 1
            {
                if (self.navController?.visibleViewController?.isEqual(ChatViewController.self))!
                {
                    
                }
                else {
                    let topVC = self.navController?.visibleViewController
                    
                    ProfileViewController.callChatDetails(userInfo["Sender_Firebase_reg_id"] as! String, topVC!)
                }
            }
            else if (push_type == 2)
            {
                self.callalertForPush(push_type!, strMsg)
            }
            else if (push_type == 3)
            {
                self.callalertForPush(push_type!, strMsg)
            }
            else if (push_type == 4)
            {
                self.callalertForPush(push_type!, strMsg)
            }
            else if (push_type == 5)
            {
                DispatchQueue.main.asyncAfter(deadline: .now() + 4.0, execute: {
                    let alert = UIAlertController(title: "BLUE BUDDY",
                                                  message: strMsg,
                                                  preferredStyle: UIAlertControllerStyle.alert)
                    
                    let cancelAction = UIAlertAction(title: "Ok",
                                                     style: .cancel, handler: nil)
                    
                    alert.addAction(cancelAction)
                    let activeViewCont = self.navController?.visibleViewController
                    activeViewCont?.present(alert, animated: true, completion: nil)
                })
            }
            else if (push_type == 8)
            {
                let alert = UIAlertController(title: "BLUE BUDDY",
                                              message: strMsg,
                                              preferredStyle: UIAlertControllerStyle.alert)
                
                let cancelAction = UIAlertAction(title: "Ok",
                                                 style: .cancel, handler: nil)
                
                alert.addAction(cancelAction)
                let activeViewCont = self.navController?.visibleViewController
                activeViewCont?.present(alert, animated: true, completion: nil)
            }
                
            else{
                
            }
        }
        else{
            if push_type == 1
            {
                
            }
            else if (push_type == 2)
            {
                
            }
            else if (push_type == 3)
            {
                
            }
            else if (push_type == 4)
            {
                
            }
            else{
                
            }
        }
        //TODO: Handle background notification
        completionHandler()
    }
    
    func convertToDictionary(text: String) -> [String: Any]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
    func callalertForPush( _ pushType : Int, _ strMsg : String)
    {
        let alert = UIAlertController(title: "BLUE BUDDY",
                                      message: strMsg,
                                      preferredStyle: UIAlertControllerStyle.alert)
        let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
            
            let tabController = (self.window!.rootViewController)?.presentedViewController as! UITabBarController
            self.navController = tabController.selectedViewController as? UINavigationController
            DispatchQueue.main.async {
                if (pushType == 1)
                {
                    let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
                    self.navController?.pushViewController(notificationVC, animated: true)
                }
                else if (pushType == 2)
                {
                    
                    let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
                    self.navController?.pushViewController(notificationVC, animated: true)
                }
                else if (pushType == 3)
                {
                    let buddyVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "BuddyListViewController") as! BuddyListViewController
                    self.navController?.pushViewController(buddyVC, animated: true)
                }
                else if (pushType == 4)
                {
                    let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
                    self.navController?.pushViewController(notificationVC, animated: true)
                }
                else{
                    
                }
            }
        })
        let cancelAction = UIAlertAction(title: "LATER",
                                         style: .cancel, handler: nil)
        alert.addAction(okAction)

        alert.addAction(cancelAction)
        let activeViewCont = self.navController?.visibleViewController
        activeViewCont?.present(alert, animated: true, completion: nil)
    }
    
}


