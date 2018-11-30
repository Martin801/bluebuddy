//
//  CreateProfileViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/1/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import Firebase
class CreateProfileViewController: UIViewController , UITextFieldDelegate{

    @IBOutlet weak var txtLastName: UITextField!
    @IBOutlet weak var txtFirstName: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: -
    // MARK: - UIBUtton Actions
    
    @IBAction func onClickBtnLogOut(_ sender: UIButton) {
        self.navigationController?.popToRootViewController(animated: true)
        UserDefaults.standard.set(false, forKey: kisLogin)
        UserDefaults.standard.set(false, forKey: kisFBLogin)
        UserDefaults.standard.set("", forKey: kUserPass)
        UserDefaults.standard.set("", forKey: kFBID)
        UserDefaults.standard.set("", forKey: kUserEmail)

    }
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        let strFirstN = txtFirstName.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        let strLastN = txtLastName.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        self.view.endEditing(true)
        if (strFirstN?.isEmpty)! {
            Common.showAlert(message: "Please enter your first name", title: "BLUE BUDDY", viewController: self)
        }
        else if (strLastN?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your last name", title: "BLUE BUDDY", viewController: self)
        }
        else{
            let dictVal = NSMutableDictionary()
            dictVal["first_name"]   = txtFirstName.text
            dictVal["last_name"]    = txtLastName.text
            dictVal["next_step"]    = "2"
                UserParser.callUpdateProfileService(dictVal, complete: { (status, strMsg) in
                    if status{
                        
                        
//                        let uid: String = (Auth.auth().currentUser?.uid)!
//                        let userData: [String : Any] = ["email" : UserDefaults.standard.value(forKey: kUserEmail)!,
//                                                        "name"  : (self.txtFirstName.text?.capitalized)! + " " + (self.txtLastName.text?.capitalized)!,
//                                                        "image" : "deafult"]
//                    Database.database().reference().child("users").child(uid).child("credentials").updateChildValues(userData, withCompletionBlock: { (errr, _) in
//                            if errr == nil {
//                                DispatchQueue.main.async {
//                                    UserDefaults.standard.set("2", forKey:kPageNo)
//
//                                    let certificationVC:UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "CertificationViewController") as UIViewController
//                                    self.navigationController?.pushViewController(certificationVC, animated: true)
//                                }
//                            }
//                        })
                        
                        DispatchQueue.main.async {
                            UserDefaults.standard.set("2", forKey:kPageNo)
                            
                            let certificationVC:UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "CertificationViewController") as UIViewController
                            self.navigationController?.pushViewController(certificationVC, animated: true)
                        }

                        
                    }
                    else{
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                })
        }
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == txtFirstName
        {
            let strFirstN = txtFirstName.text?.trimmingCharacters(in: .whitespacesAndNewlines)
            if (strFirstN?.isEmpty)!
            {
                txtFirstName.text = nil
            }
        }
        else if textField == txtLastName
        {
            let strLastN = txtLastName.text?.trimmingCharacters(in: .whitespacesAndNewlines)
            if (strLastN?.isEmpty)!
            {
                txtLastName.text = nil
            }
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

}
