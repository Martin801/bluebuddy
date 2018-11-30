//
//  OTPVerificationViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/1/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import MICountryPicker
class OTPVerificationViewController: UIViewController {
    
    @IBOutlet weak var cnsTopLblVerfiy: NSLayoutConstraint!
    @IBOutlet weak var imgFlag: UIImageView!
    @IBOutlet weak var lblCode: UILabel!
    @IBOutlet weak var txtPhoneNo: UITextField!
    @IBOutlet weak var viewPopUp: UIView!
    @IBOutlet weak var btnPhoneCode: UIButton!
    var strOpt = String()
    var isPopUpOpen = Bool()
    var strPhoneCode = String()
    @IBOutlet weak var txtCode: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
   //     txtCode.text = strOpt
        viewPopUp.isHidden = isPopUpOpen ? false : true
        btnPhoneCode.contentHorizontalAlignment = .right
        imgFlag.image = UIImage(named: "assets.bundle/" + "us" + ".png", in: Bundle(for: MICountryPicker.self), compatibleWith: nil)
        strPhoneCode = "+1"
        // Do any additional setup after loading the view.
        
        print("screenType:", UIDevice.current.screenType.rawValue)
        if UIDevice.current.screenType.rawValue == "iPhone 4 or iPhone 4S" {
            cnsTopLblVerfiy.constant = 0
            
        }
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    // MARK: -
    // MARK: - UIBUtton Actions
    @IBAction func onClickBtnSubmit(_ sender: UIButton) {
        self.view.endEditing(true)
        if (txtCode.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your otp", title: "BLUE BUDDY", viewController: self)
        }
        else
        {
            let dictVal = NSMutableDictionary()
            dictVal["otp"] = txtCode.text
            dictVal["user_id"] = UserDefaults.standard.value(forKey: kUserID)

            UserParser.callActivateUserService(dictVal, complete: { (status, strMsg) in
                if status
                {
                    UserDefaults.standard.set(true, forKey: kisLogin)
                    DispatchQueue.main.async {
                        let profileVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CreateProfileViewController") as! CreateProfileViewController
                        self.navigationController?.pushViewController(profileVC, animated: true)
                    }
                }
                else{
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    
                }
            })
        }
     }
    
    @IBAction func btnActionOpenResendView(_ sender: Any) {
        
        viewPopUp.isHidden = false
        
    }
    @IBAction func onClickBtnPhCode(_ sender: UIButton) {
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
    @IBAction func onClickSendMobileNo(_ sender: UIButton) {
        self.view.endEditing(true)
        if (txtPhoneNo.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your registerd phone number", title: "BLUE BUDDY", viewController: self)
        }
        else{
            let strPhone = self.strPhoneCode + self.txtPhoneNo.text!
            let arr = strPhone.characters.split(separator: " ").map(String.init)
            let dictVal = NSMutableDictionary()
            dictVal["mobile_no"] = arr.joined(separator: "")
            dictVal["user_id"] = UserDefaults.standard.value(forKey: kUserID)
            self.txtPhoneNo.text = ""
            UserParser.callsendMobileNoForVerificationService(dictVal, complete: { (status, strMsg) in
                
                if strMsg != "" {
                    DispatchQueue.main.async {
                        self.viewPopUp.isHidden = true
                    }
                    
                }
                
            })
        }
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

extension OTPVerificationViewController: MICountryPickerDelegate {
    func countryPicker(_ picker: MICountryPicker, didSelectCountryWithName name: String, code: String) {
        self.navigationController?.navigationBar.isHidden = true
        picker.navigationController?.popViewController(animated: true)
        //        self.dismiss(animated: true, completion: nil)
    }
}
