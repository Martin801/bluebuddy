//
//  LoginViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/1/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import SafariServices
import FacebookCore
import FacebookLogin
import Firebase
import NVActivityIndicatorView

let APP_DELEGATE = UIApplication.shared.delegate as! AppDelegate

class LoginViewController: BaseViewController, UITextViewDelegate, SFSafariViewControllerDelegate {

    @IBOutlet weak var txtVwTerms: UITextView!
    @IBOutlet weak var vwTerms: UIView!
    @IBOutlet weak var iconCheckBox: UIImageView!
    var strTitle:String = "PROFILE"
    @IBOutlet weak var txtTerms: UITextView!
    @IBOutlet weak var btnTick: UIButton!
    @IBOutlet weak var txtPassword: UITextField!
    @IBOutlet weak var txtEmail: UITextField!
    @IBOutlet weak var view_hieght: NSLayoutConstraint!
    
    override func viewDidLoad() {
        super.viewDidLoad()
//        setLabelTitleText(strTitle)
//        self.designTabBarwithSelectedIndex(selectedIndex: 3)
        txtTerms.linkTextAttributes = [NSForegroundColorAttributeName: UIColor.darkGray]
        let linkAttributes = [
            NSLinkAttributeName: NSURL(string: "https://www.apple.com")!,
            NSForegroundColorAttributeName: UIColor.darkGray, NSFontAttributeName: txtTerms.font!
            
            ] as [String : Any]
        
        let attributedString = NSMutableAttributedString(string: "By clicking, you are accepting the Terms and Conditions")
        
        txtTerms.isEditable = false
        txtVwTerms.isEditable = false
        // Set the 'click here' substring to be the link
//        attributedString.setAttributes(linkAttributes, range: NSMakeRange(0, attributedString.length))
        
        txtTerms.delegate = self
        attributedString.addAttribute(NSUnderlineStyleAttributeName, value: NSUnderlineStyle.styleSingle.rawValue, range: NSMakeRange(0, attributedString.length))
        
        txtTerms.attributedText = attributedString

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: -
    // MARK: - UIBUtton Actions
    @IBAction func onCkickBtnRegister(_ sender: UIButton) {
        
        let registerVC:UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "RegisterViewController") as UIViewController
        self.navigationController?.pushViewController(registerVC, animated: true)
        
    }
    @IBAction func onClickBtnFBLogin(_ sender: UIButton) {
        
        let loginManager = LoginManager()
        loginManager.logOut()
        loginManager.logIn([ .publicProfile, .email ], viewController: self) { loginResult in
            switch loginResult {
            case .failed(let error):
                print(error)
            case .cancelled:
                print("User cancelled login.")
            case .success(let grantedPermissions, _, _):
                if AccessToken.current != nil {
                    let params = ["fields" : "email, name, first_name,last_name, picture.width(1000).height(1000)"]
                    let graphRequest = GraphRequest(graphPath: "me", parameters: params)
                    graphRequest.start {
                        (urlResponse, requestResult) in
                        
                        switch requestResult {
                        case .failed(let error):
                            print("error in graph request:", error)
                            break
                        case .success(let graphResponse):
                            if let responseDictionary = graphResponse.dictionaryValue {
                                let objProfile = ProfileBO()
                                objProfile.first_name = responseDictionary["first_name"] as! String
                                objProfile.last_name = responseDictionary["last_name"] as! String
                                objProfile.userId = responseDictionary["id"] as! String
                                objProfile.email = responseDictionary["email"] as! String
                                let dict = responseDictionary["picture"] as! NSDictionary
                                let data = dict["data"] as! NSDictionary
                                
                                if responseDictionary["email"] as! String != "" {
                                    
                                    objProfile.profile_pic = data["url"] as! String
                                    //    print(responseDictionary["picture"])
                                    let credential = FacebookAuthProvider.credential(withAccessToken: (AccessToken.current?.authenticationToken)!)
                                    let activityData = ActivityData()
                                    NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
                                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                                        NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
                                    }
                                    
                                    let dictVal = NSMutableDictionary()
                                    dictVal["social_id"] = objProfile.userId
                                    dictVal["social"] = "facebook"
                                    dictVal["email"] = objProfile.email;
//                                    dictVal["fname"] = objProfile.first_name;
//                                    dictVal["lname"] = objProfile.last_name
                                    dictVal["firebase_api_key"]    = UserDefaults.standard.value(forKey: kUserDeviceToken)
                                    dictVal["device_type"] = "ios"
                                    dictVal["is_social"] = "1"
                                    dictVal["password"] = ""
                                    
                                    UserParser.callLoginUserService(dictVal, isLoader: true, complete: { (status, strMsg, strUserStatus, dictResult) in
                                        
                                        if status{
                                            DispatchQueue.main.async {
                                                
                                                let dictUserData = dictResult.value(forKey: "user_details") as! NSDictionary
                                                
                                                Auth.auth().signIn(withEmail: dictUserData["Email"] as! String, password: dictUserData["Password"] as! String, completion: { (user, error) in
                                                    if let err:Error = error {
                                                        Common.showAlert(message: String(err.localizedDescription), title: "BLUE BUDDY", viewController: self)
                                                    }
                                                    else{
                                                        let stepNo = UserDefaults.standard.value(forKey: kPageNo) as? String
                                                        
                                                        UserDefaults.standard.set(true, forKey: kisLogin)
                                                        DispatchQueue.main.async {
                                                            UserDefaults.standard.set(dictUserData["Email"] as! String, forKey: kUserEmail)
                                                            UserDefaults.standard.set(dictUserData["Password"] as! String, forKey: kUserPass)
                                                            self.callStepNo(stepNo!)
                                                        }
                                                        
                                                    }
                                                })
                                            }
                                        }
                                        else if strUserStatus == "0"
                                        {
                                            DispatchQueue.main.async {
                                                let otpVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "OTPVerificationViewController") as? OTPVerificationViewController
                                                otpVC?.strOpt = ""
                                                otpVC?.isPopUpOpen = true
                                                self.navigationController?.pushViewController(otpVC!, animated: true)
                                                
                                            }
                                        }
                                        else{
                                            DispatchQueue.main.async {
                                                //  Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                                                
                                                //send to the registration page with the email prefilled in
                                                let registerVC:RegisterViewController = self.storyboard!.instantiateViewController(withIdentifier: "RegisterViewController") as! RegisterViewController
                                                registerVC.strEmail = objProfile.email;
                                                registerVC.strFacebookID = objProfile.userId;
                                                registerVC.isFromFbLogin = true
                                                self.navigationController?.pushViewController(registerVC, animated: true)
                                            }
                                            
                                        }
                                    })
                                    
                                }
                                else {
                                    Common.showAlert(message: "", title: "No email found", viewController: self)
                                }
                                
                                
                                
                                    }
                    }
                }
            }
        }
        
        //This is commented out as the new login feature is implemented, but the code is kept for future reference
        
       /* let loginManager = LoginManager()
        loginManager.logIn([ .publicProfile, .email ], viewController: self) { loginResult in
            switch loginResult {
            case .failed(let error):
                print(error)
            case .cancelled:
                print("User cancelled login.")
            case .success(let grantedPermissions, _, _):
                if AccessToken.current != nil {
                    let params = ["fields" : "email, name, first_name,last_name, picture.width(1000).height(1000)"]
                    let graphRequest = GraphRequest(graphPath: "me", parameters: params)
                    graphRequest.start {
                        (urlResponse, requestResult) in
                        
                        switch requestResult {
                        case .failed(let error):
                            print("error in graph request:", error)
                            break
                        case .success(let graphResponse):
                            if let responseDictionary = graphResponse.dictionaryValue {
                                let objProfile = ProfileBO()
                                objProfile.first_name = responseDictionary["first_name"] as! String
                                objProfile.last_name = responseDictionary["last_name"] as! String
                                objProfile.userId = responseDictionary["id"] as! String
                                objProfile.email = responseDictionary["email"] as! String
                                let dict = responseDictionary["picture"] as! NSDictionary
                                let data = dict["data"] as! NSDictionary
                                
                                objProfile.profile_pic = data["url"] as! String
                            //    print(responseDictionary["picture"])
                                let credential = FacebookAuthProvider.credential(withAccessToken: (AccessToken.current?.authenticationToken)!)
                                let activityData = ActivityData()
                                NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
                                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                                    NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
                                }
                                Auth.auth().signIn(with: credential, completion: { (User, error) in
                                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                                        NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
                                    }
                                    if error != nil
                                    {
                                        Common.showAlert(message: String(describing: error?.localizedDescription), title: "BLUE BUDDY", viewController: self)
                                        print(error?.localizedDescription as Any)
                                    }
                                    else{
                                        let dictVal = NSMutableDictionary()
                                        dictVal["social_id"] = objProfile.userId
                                        dictVal["social"] = "facebook"
                                        dictVal["fname"] = objProfile.first_name;
                                        dictVal["lname"] = objProfile.last_name
                                        dictVal["firebase_api_key"]    = UserDefaults.standard.value(forKey: kUserDeviceToken)
                                        dictVal["device_type"] = "ios"
                                        UserParser.callLoginWithFacebook(dictVal, isLoader: true, complete: { (status, strMsg, new_socialuser) in
                                            DispatchQueue.main.async {
                                                if new_socialuser
                                                {
                                                    let profileVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CreateProfileFBViewController") as! CreateProfileFBViewController
                                                    profileVC.objPro = objProfile
                                                    UserDefaults.standard.set(objProfile.userId, forKey: kFBID)
                                                    self.navigationController?.pushViewController(profileVC, animated: true)
                                                }
                                                else if status{
                                                    UserDefaults.standard.set(true, forKey: kisFBLogin)
                                                    UserDefaults.standard.set(objProfile.userId, forKey: kFBID)
                                                    let stepNo = UserDefaults.standard.value(forKey: kPageNo) as? String
                                                    self.callStepNo(stepNo!)
                                                }
                                                else{
                                                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                                                }
                                            }
                                        })
                                    }
                                })
                            }
                        }
                    }
                }
            }
        } */
    }
    }

    @IBAction func onClickBtnLogin(_ sender: UIButton) {
        self.view.endEditing(true)
        if (txtEmail.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your email id", title: "BLUE BUDDY", viewController: self)
        }
        else if !isValidEmail(testStr: txtEmail.text!)
        {
            Common.showAlert(message: "This email address is not valid", title: "BLUE BUDDY", viewController: self)
        }
        
        else if (txtPassword.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your password", title: "BLUE BUDDY", viewController: self)
        }
        else if !btnTick.isSelected
        {
            Common.showAlert(message: "Please check the terms & condition", title: "BLUE BUDDY", viewController: self)
        }
        else{
            let dictVal = NSMutableDictionary()
            dictVal["email"] = txtEmail.text
            dictVal["password"] = txtPassword.text
            dictVal["firebase_api_key"] = UserDefaults.standard.value(forKey: kUserDeviceToken)
            dictVal["device_type"] = "ios"
            dictVal["is_social "] = "0"
            dictVal["social"] = ""
            dictVal["social_id"] = ""
            UserParser.callLoginUserService(dictVal, isLoader: true, complete: { (status, strMsg, strUserStatus, dictResult) in
                
                if status{
                    DispatchQueue.main.async {
                        Auth.auth().signIn(withEmail: self.txtEmail.text!, password: self.txtPassword.text!, completion: { (user, error) in
                            if let err:Error = error {
                                Common.showAlert(message: String(err.localizedDescription), title: "BLUE BUDDY", viewController: self)
                            }
                            else{
                                let stepNo = UserDefaults.standard.value(forKey: kPageNo) as? String
                                
                                UserDefaults.standard.set(true, forKey: kisLogin)
                                DispatchQueue.main.async {
                                    UserDefaults.standard.set(self.txtEmail.text, forKey: kUserEmail)
                                    UserDefaults.standard.set(self.txtPassword.text, forKey: kUserPass)
                                    self.callStepNo(stepNo!)
                                }

                            }
                        })
                    }
                                    }
                else if strUserStatus == "0"
                {
                    DispatchQueue.main.async {
                        let otpVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "OTPVerificationViewController") as? OTPVerificationViewController
                        otpVC?.strOpt = ""
                        otpVC?.isPopUpOpen = true
                        self.navigationController?.pushViewController(otpVC!, animated: true)
                        
                    }
                }
                else{
                        DispatchQueue.main.async {
                            Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                        }
                    
                }
            })
        }
    }
    @IBAction func onClickBtnForgetPass(_ sender: UIButton) {
        let forgotPassVC:UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "ForgotPassViewController") as UIViewController
        self.navigationController?.pushViewController(forgotPassVC, animated: true)
    }
    @IBAction func onClickBtnTick(_ sender: UIButton) {
        sender.isSelected = !sender.isSelected
        iconCheckBox.image = sender.isSelected ? #imageLiteral(resourceName: "check.png") : #imageLiteral(resourceName: "square.png")
    }

    // MARK: -
    // MARK: - TextView Delegates
    func textView(_ textView: UITextView, shouldInteractWith URL: URL, in characterRange: NSRange, interaction: UITextItemInteraction) -> Bool {
        let safariVC = SFSafariViewController(url: URL)
        self.present(safariVC, animated: true, completion: nil)
        safariVC.delegate = self
        return false
    }
    
    // MARK: -
    // MARK: - SafariViewController Delegates
    func safariViewControllerDidFinish(_ controller: SFSafariViewController) {
        controller.dismiss(animated: true, completion: nil)
    }
    
    func isValidEmail(testStr:String) -> Bool {
        print("validate emilId: \(testStr)")
        let emailRegEx = "^(?:(?:(?:(?: )*(?:(?:(?:\\t| )*\\r\\n)?(?:\\t| )+))+(?: )*)|(?: )+)?(?:(?:(?:[-A-Za-z0-9!#$%&’*+/=?^_'{|}~]+(?:\\.[-A-Za-z0-9!#$%&’*+/=?^_'{|}~]+)*)|(?:\"(?:(?:(?:(?: )*(?:(?:[!#-Z^-~]|\\[|\\])|(?:\\\\(?:\\t|[ -~]))))+(?: )*)|(?: )+)\"))(?:@)(?:(?:(?:[A-Za-z0-9](?:[-A-Za-z0-9]{0,61}[A-Za-z0-9])?)(?:\\.[A-Za-z0-9](?:[-A-Za-z0-9]{0,61}[A-Za-z0-9])?)*)|(?:\\[(?:(?:(?:(?:(?:[0-9]|(?:[1-9][0-9])|(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5]))\\.){3}(?:[0-9]|(?:[1-9][0-9])|(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5]))))|(?:(?:(?: )*[!-Z^-~])*(?: )*)|(?:[Vv][0-9A-Fa-f]+\\.[-A-Za-z0-9._~!$&'()*+,;=:]+))\\])))(?:(?:(?:(?: )*(?:(?:(?:\\t| )*\\r\\n)?(?:\\t| )+))+(?: )*)|(?: )+)?$"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        let result = emailTest.evaluate(with: testStr)
        return result
    }
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    class func callProfileDetails() {
        DispatchQueue.main.async {
            UserParser.callProfileDetailsService("", complete: { (status, strMsg, objProfile) in
                if status
                {
                    DispatchQueue.main.async {
                        let newViewController = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "TabViewController") as! TabViewController
                        newViewController.selectedIndex = 4
                        APP_DELEGATE.navController?.pushViewController(newViewController, animated: true)
                        APP_DELEGATE.navController?.interactivePopGestureRecognizer?.isEnabled = false
                        //                    self.present(newViewController, animated: false, completion: nil)
                    }
                    
                }
                
            })
        }
    }
    func callStepNo( _ stepNo :String)
    {
        if stepNo == "1"
        {
            let profileVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CreateProfileViewController") as! CreateProfileViewController
            self.navigationController?.pushViewController(profileVC, animated: true)
        }
        else if (stepNo == "2")
        {
            let certificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationViewController") as! CertificationViewController
            self.navigationController?.pushViewController(certificationVC, animated: true)
        }
        else if (stepNo == "11")
        {
            let certificationDetailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationDetailsViewController") as? CertificationDetailsViewController
            certificationDetailsVC?.strSelectedItem = 1
            self.navigationController?.pushViewController(certificationDetailsVC!, animated: true)
        }
        else if (stepNo == "12")
        {
            let freeDiverVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "FreeDiverCertificationDetailsViewController") as! FreeDiverCertificationDetailsViewController
            self.navigationController?.pushViewController(freeDiverVC, animated: true)
        }
        else if (stepNo == "4")
        {
            let aboutVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AboutYourselfViewController") as! AboutYourselfViewController
            self.navigationController?.pushViewController(aboutVC, animated: true)
        }
        else if (stepNo == "5")
        {
            let socialVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddSocialLinkViewController") as! AddSocialLinkViewController
            self.navigationController?.pushViewController(socialVC, animated: true)
        }
        else if (stepNo == "6")
        {
            let aboutVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "OtherLinkViewController") as! OtherLinkViewController
            self.navigationController?.pushViewController(aboutVC, animated: true)
        }
        else if (stepNo == "7")
        {
            let locationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "LocationViewController") as! LocationViewController
            self.navigationController?.pushViewController(locationVC, animated: true)
        }
        else if (stepNo == "8")
        {
            let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "UploadPicViewController") as! UploadPicViewController
            self.navigationController?.pushViewController(uploadVC, animated: true)
        }
        else if (stepNo == "9")
        {
            LoginViewController.callProfileDetails()
        }
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    @IBAction func closeTermsAction(_ sender: Any) {
        
        vwTerms.isHidden = true
    }
    
    @IBAction func openTermsAction(_ sender: Any) {
        
        vwTerms.isHidden = false
    }
}
