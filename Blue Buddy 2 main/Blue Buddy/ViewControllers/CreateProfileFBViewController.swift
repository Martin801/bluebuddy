//
//  CreateProfileFBViewController.swift
//  Blue Buddy
//
//  Created by AQUARIOUS on 29/01/18.
//  Copyright © 2018 Aquatech iOS. All rights reserved.
//

import UIKit
import MICountryPicker
import Firebase

class CreateProfileFBViewController: UIViewController, MICountryPickerDelegate {

    @IBOutlet weak var imgFlag: UIImageView!
    @IBOutlet weak var lblCode: UILabel!
    @IBOutlet weak var btnPhoneCode: UIButton!
    @IBOutlet weak var txtPhoneNo: UITextField!
    @IBOutlet weak var txtEmail: UITextField!
    @IBOutlet weak var txtLastName: UITextField!
    @IBOutlet weak var txtFirstName: UITextField!
    var strPhoneCode = String()
    var objPro = ProfileBO ()
    override func viewDidLoad() {
        super.viewDidLoad()

        btnPhoneCode.contentHorizontalAlignment = .right
        imgFlag.image = UIImage(named: "assets.bundle/" + "us" + ".png", in: Bundle(for: MICountryPicker.self), compatibleWith: nil)
        strPhoneCode = "+ 01"
        txtFirstName.text = objPro.first_name
        txtLastName.text = objPro.last_name
        if !(objPro.email.isEmpty)
        {
            txtEmail.text = objPro.email
        }
        
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: -
    // MARK: - UIButton Action Methods
    @IBAction func onClickBtnPhoneCode(_ sender: UIButton) {
        let picker = MICountryPicker { (name, code) -> () in
            print(code)
        }
        
        picker.delegate = self
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
    }
    
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        
        view.endEditing(true)
        let strPhone = self.strPhoneCode + self.txtPhoneNo.text!
        //            let arr = strPhone.characters.split(" ").map(String.init)
        let arr = strPhone.characters.split(separator: " ").map(String.init)
        
        if (txtFirstName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your first name", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtLastName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your last name", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtEmail.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your email id", title: "BLUE BUDDY", viewController: self)
        }
        else if !isValidEmail(testStr: txtEmail.text!)
        {
            Common.showAlert(message: "This email address is not valid", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtPhoneNo.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your phone no", title: "BLUE BUDDY", viewController: self)
        }
        else if !(Common.isValidPhone(testStr: arr.joined(separator: ""))) {
            Common.showAlert(message: "Please enter a valid phone no", title: "BLUE BUDDY", viewController: self)
        }
        else{

            let dictVal = NSMutableDictionary()
            dictVal["first_name"]   = txtFirstName.text
            dictVal["last_name"]    = txtLastName.text
            dictVal["email"]    = txtEmail.text
            dictVal["mobile_no"]    = arr.joined(separator: "")
            dictVal["firebase_reg_id"]    = (Auth.auth().currentUser?.uid)!
            dictVal["firebase_api_key"]    = UserDefaults.standard.value(forKey: kUserDeviceToken)
            dictVal["next_step"]    = "2"
            UserParser.callUpdateProfileService(dictVal, complete: { (status, strMsg) in
                if status{
                    UserDefaults.standard.set(self.txtEmail.text, forKey: kUserEmail)
                    DispatchQueue.main.async {
                        let uid: String = (Auth.auth().currentUser?.uid)!
                        let userData: [String : Any] = ["email" : UserDefaults.standard.value(forKey: kUserEmail)!,
                                                        "name"  : (self.txtFirstName.text?.capitalized)! + " " + (self.txtLastName.text?.capitalized)!,
                                                        "image" : "default"]
                        Database.database().reference().child("users").child(uid).child("credentials").updateChildValues(userData, withCompletionBlock: { (errr, _) in
                            if errr == nil {
                                DispatchQueue.main.async {
                                    UserDefaults.standard.set("2", forKey:kPageNo)
                                    UserDefaults.standard.set(true, forKey: kisFBLogin)
                                    let certificationVC:UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "CertificationViewController") as UIViewController
                                    self.navigationController?.pushViewController(certificationVC, animated: true)
                                }
                            }
                        })
                    }
                }
                else{
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            })
        }
    }
    
    // MARK: -
    // MARK: - Other Methods
    func countryPicker(_ picker: MICountryPicker, didSelectCountryWithName name: String, code: String)
    {
        
    }
    
    func isValidEmail(testStr:String) -> Bool {
        print("validate emilId: \(testStr)")
        let emailRegEx = "^(?:(?:(?:(?: )*(?:(?:(?:\\t| )*\\r\\n)?(?:\\t| )+))+(?: )*)|(?: )+)?(?:(?:(?:[-A-Za-z0-9!#$%&’*+/=?^_'{|}~]+(?:\\.[-A-Za-z0-9!#$%&’*+/=?^_'{|}~]+)*)|(?:\"(?:(?:(?:(?: )*(?:(?:[!#-Z^-~]|\\[|\\])|(?:\\\\(?:\\t|[ -~]))))+(?: )*)|(?: )+)\"))(?:@)(?:(?:(?:[A-Za-z0-9](?:[-A-Za-z0-9]{0,61}[A-Za-z0-9])?)(?:\\.[A-Za-z0-9](?:[-A-Za-z0-9]{0,61}[A-Za-z0-9])?)*)|(?:\\[(?:(?:(?:(?:(?:[0-9]|(?:[1-9][0-9])|(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5]))\\.){3}(?:[0-9]|(?:[1-9][0-9])|(?:1[0-9][0-9])|(?:2[0-4][0-9])|(?:25[0-5]))))|(?:(?:(?: )*[!-Z^-~])*(?: )*)|(?:[Vv][0-9A-Fa-f]+\\.[-A-Za-z0-9._~!$&'()*+,;=:]+))\\])))(?:(?:(?:(?: )*(?:(?:(?:\\t| )*\\r\\n)?(?:\\t| )+))+(?: )*)|(?: )+)?$"
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        let result = emailTest.evaluate(with: testStr)
        return result
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
