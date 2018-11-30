//
//  RegisterViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/1/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import MICountryPicker
import SafariServices
import Firebase
import NVActivityIndicatorView
class RegisterViewController: UIViewController, UITextViewDelegate, SFSafariViewControllerDelegate, UITextFieldDelegate{

    @IBOutlet weak var iconCheckBox: UIImageView!
    @IBOutlet weak var txtTerms: UITextView!
    @IBOutlet weak var imgFlag: UIImageView!
    @IBOutlet weak var lblCode: UILabel!
    @IBOutlet weak var btnPhoneCode: UIButton!
    @IBOutlet weak var btnTick: UIButton!
    @IBOutlet weak var txtConfirmPass: UITextField!
    @IBOutlet weak var txtPass: UITextField!
    @IBOutlet weak var txtPhoneNo: UITextField!
    @IBOutlet weak var txtEmail: UITextField!
    var strPhoneCode = String()
    var strFacebookID = String()
    var strEmail = String()
    var isFromFbLogin = false
    override func viewDidLoad() {
        super.viewDidLoad()
       
//        btnPhoneCode.semanticContentAttribute = .forceRightToLeft
        btnPhoneCode.contentHorizontalAlignment = .right
        imgFlag.image = UIImage(named: "assets.bundle/" + "us" + ".png", in: Bundle(for: MICountryPicker.self), compatibleWith: nil)
        txtTerms.linkTextAttributes = [NSForegroundColorAttributeName: UIColor.darkGray]
        let linkAttributes = [
            NSLinkAttributeName: NSURL(string: "http://thebluebuddy.com/privacy-policy.php")!,
            NSForegroundColorAttributeName: UIColor.darkGray, NSFontAttributeName: txtTerms.font!
            
            ] as [String : Any]
        
        let attributedString = NSMutableAttributedString(string: "By clicking you have accepted terms & condition")
        
        // Set the 'click here' substring to be the link
        attributedString.setAttributes(linkAttributes, range: NSMakeRange(0, attributedString.length))
        
        txtTerms.delegate = self
        attributedString.addAttribute(NSUnderlineStyleAttributeName, value: NSUnderlineStyle.styleSingle.rawValue, range: NSMakeRange(0, attributedString.length))
        
        txtTerms.attributedText = attributedString
        strPhoneCode = "+1"

        // Do any additional setup after loading the view.
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(true)
        
        if isFromFbLogin {
            self.txtEmail.text = strEmail
            self.txtEmail.isUserInteractionEnabled = false
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    // MARK: -
    // MARK: - UIBUtton Actions
    @IBAction func onClickBtnTick(_ sender: UIButton) {
        sender.isSelected = !sender.isSelected
        iconCheckBox.image = sender.isSelected ? #imageLiteral(resourceName: "check.png") : #imageLiteral(resourceName: "square.png")

    }
    @IBAction func onClickBtnPhoneCode(_ sender: UIButton) {
        let picker = MICountryPicker { (name, code) -> () in
            print(code)
        }
        
        // Optional: To pick from custom countries list
        //        picker.customCountriesCode = ["EG", "US", "AF", "AQ", "AX"]
        
        // delegate
        picker.delegate = self
        
        // Display calling codes
        //        picker.showCallingCodes = true
        
        // or closure
        picker.didSelectCountryClosure = { name, code in
            print(code)
        }
        picker.didSelectCountryWithCallingCodeClosure = { names, code, dialCode, img in
            self.navigationController?.navigationBar.isHidden = true
            self.lblCode.text = "\(code) \(dialCode)"
            self.imgFlag.image = img
            self.strPhoneCode = "\(dialCode)"
           print(dialCode)
        }
        self.navigationController?.pushViewController(picker, animated: true)
        self.navigationController?.navigationBar.isHidden = true

//        self.present(picker, animated: true, completion: nil)
    }

    @IBAction func onClickBtnRegister(_ sender: UIButton) {
        view.endEditing(true)
        
        let strPhone = self.strPhoneCode + self.txtPhoneNo.text!
        //            let arr = strPhone.characters.split(" ").map(String.init)
        let arr = strPhone.characters.split(separator: " ").map(String.init)
        
//        if (txtEmail.text?.isEmpty)! {
//            Common.showAlert(message: "Please enter your email id", title: "BLUE BUDDY", viewController: self)
//        }
//        else if !isValidEmail(testStr: txtEmail.text!)
//        {
//            Common.showAlert(message: "This email address is not valid", title: "BLUE BUDDY", viewController: self)
//        }
//        else if (txtPhoneNo.text?.isEmpty)! {
//            Common.showAlert(message: "Please enter your phone no", title: "BLUE BUDDY", viewController: self)
//        }
        if !(Common.isValidPhone(testStr: arr.joined(separator: ""))) {
            Common.showAlert(message: "Please enter a valid phone no", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtPass.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your password", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtConfirmPass.text?.isEmpty)! {
            Common.showAlert(message: "Please re-enter your password", title: "BLUE BUDDY", viewController: self)
        }
        else if !btnTick.isSelected
        {
            Common.showAlert(message: "Please check the terms & condition", title: "BLUE BUDDY", viewController: self)
        }
        else{

            let activityData = ActivityData()
            NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
            }
            
//            Auth.auth().createUser(withEmail: txtEmail.text!, password: txtPass.text!, completion: { (user, error) in
//                if let err:Error = error {
//                    DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
//                        NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
//                    }
//                    Common.showAlert(message: String(err.localizedDescription), title: "BLUE BUDDY", viewController: self)
//                    print(err.localizedDescription)
//                  //  return
//                }
//                else{
//                    let uid: String = (Auth.auth().currentUser?.uid)!
                    let arrNames = self.txtEmail.text?.components(separatedBy: "@")
                    let values = ["name": arrNames![0], "email": self.txtEmail.text!, "image": "default"]
//                    Database.database().reference().child("users").child((user?.uid)!).child("credentials").updateChildValues(values, withCompletionBlock: { (errr, _) in
//                        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
//                            NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
//                        }
//                        if errr == nil {
            
            
//                            let strPhone = self.strPhoneCode + self.txtPhoneNo.text!
//                            //            let arr = strPhone.characters.split(" ").map(String.init)
//                            let arr = strPhone.characters.split(separator: " ").map(String.init)
                            
                            let dictVal = NSMutableDictionary()
                            dictVal["email"] = self.txtEmail.text
                            dictVal["mobile_no"] = arr.joined(separator: "")
                            dictVal["password"] = self.txtPass.text
//                            dictVal["firebase_reg_id"] = uid
//                            dictVal["firebase_api_key"] = UserDefaults.standard.value(forKey: kUserDeviceToken)
                            dictVal["device_type"] = "ios"
            
            
                            if isFromFbLogin {
                                dictVal["social_id"] = strFacebookID
                                dictVal["social"] = "facebook"
                                dictVal["is_social"] = "1"
                            }
                            else {
                                dictVal["is_social"] = "0"
                                dictVal["social"] = ""
                                dictVal["social_id"] = ""
                            }
            
                            //user_id
                            //email, mobile_no, password,firebase_api_key, firebase_reg_id
                            
                            UserParser.callSignUpService(dictPram: dictVal as NSDictionary, complete: { (isStatus, strMsg, strOTP) in
                                if isStatus{
                                    UserDefaults.standard.set(self.txtEmail.text, forKey: kUserEmail)
                                    UserDefaults.standard.set(self.txtPass.text, forKey: kUserPass)
                                    UserDefaults.standard.set(true, forKey: kisLogin)
                                    DispatchQueue.main.async {
                                        let otpVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "OTPVerificationViewController") as? OTPVerificationViewController
                                        otpVC?.strOpt = strOTP
                                        otpVC?.isPopUpOpen = false
                                        self.navigationController?.pushViewController(otpVC!, animated: true)
                                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                                            Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                                        }
                                    }
                                }
                                else{
                                    DispatchQueue.main.async {
                                        let user = Auth.auth().currentUser
                                        user?.delete(completion: { (error) in
                                            if error != nil {
                                                // An error happened.
                                                user?.delete(completion: { (error) in
                                                    if error != nil {
                                                        // An error happened.
//                                                        Common.showAlert(message: error?.localizedDescription, title: "BLUE BUDDY", viewController: self)
                                                    } else {
                                                        // Account deleted.
                                                    }
                                                })

                                            } else {
                                                // Account deleted.
                                            }
                                        })
                                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                                    }
                                }
                            })
//                        }
//                    })
//                }
//            })
        }
    }

    @IBAction func onClickBtnLogin(_ sender: UIButton) {
    
        var isFound:Bool = false
        if let viewControllers = navigationController?.viewControllers {
            for viewController in viewControllers {
                // some process
                if viewController.isKind(of: LoginViewController.self) {
                    print("yes it is")
                    isFound = true
                    self.navigationController?.popToViewController(viewController, animated: true)
                }
            }
            if !isFound
            {
                let loginVC:UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "LoginViewController") as UIViewController
                self.navigationController?.pushViewController(loginVC, animated: true)
            }
        }
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
    // MARK: - TextField Delegates
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == txtPhoneNo
        {
            let currentText = textField.text ?? ""
            guard let stringRange = Range(range, in: currentText) else { return false }
            let updatedText = currentText.replacingCharacters(in: stringRange, with: string)
            if updatedText.count <= 15
            {
                return true
            }
            else
            {
                return false
            }
        }
        else
        {
            return true
        }
    }
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == txtConfirmPass
        {
            if txtPass.text == txtConfirmPass.text
            {
                
            }
            else
            {
                Common.showAlert(message: "Password does not match the confirm password", title: "BLUE BUDDY", viewController: self)
                txtConfirmPass.becomeFirstResponder()
            }
        }
        
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


    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}

extension RegisterViewController: MICountryPickerDelegate {
    func countryPicker(_ picker: MICountryPicker, didSelectCountryWithName name: String, code: String) {
        self.navigationController?.navigationBar.isHidden = true
        picker.navigationController?.popViewController(animated: true)
//        self.dismiss(animated: true, completion: nil)
    }
}

