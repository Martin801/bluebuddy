//
//  AboutYourselfViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/2/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class
AboutYourselfViewController: UIViewController, UITextViewDelegate {

    @IBOutlet weak var imgBorder: UIImageView!
    @IBOutlet weak var txtViewDesc: UITextView!
    override func viewDidLoad() {
        super.viewDidLoad()
        let myColor = UIColor.lightGray
        imgBorder.layer.borderColor = myColor.cgColor
        imgBorder.layer.borderWidth = 1.0

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: -
    // MARK: - UIButton Action Methods
    
    @IBAction func onClickBtnLogOut(_ sender: UIButton) {
        self.navigationController?.popToRootViewController(animated: true)
        UserDefaults.standard.set(false, forKey: kisLogin)
        UserDefaults.standard.set(false, forKey: kisFBLogin)
        UserDefaults.standard.set("", forKey: kFBID)
        UserDefaults.standard.set("", forKey: kUserPass)
        UserDefaults.standard.set("", forKey: kUserEmail)

    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        self.view.endEditing(true)
        if (txtViewDesc.text?.isEmpty)! {
            Common.showAlert(message: "Please write about yourself", title: "BLUE BUDDY", viewController: self)
        }
        else{
            let dictVal = NSMutableDictionary()
            dictVal["about"] = txtViewDesc.text
            dictVal["next_step"] = "5"
            UserDefaults.standard.set("5", forKey:kPageNo)
            UserParser.callUpdateProfileService(dictVal, complete: { (status, strMsg) in
                if status
                {
                    DispatchQueue.main.async {
                        let socialVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddSocialLinkViewController") as? AddSocialLinkViewController
                        self.navigationController?.pushViewController(socialVC!, animated: true)
                        
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
    
    
    // MARK: -
    // MARK: - UITextViewDelegates
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        txtViewDesc.text = ""
        txtViewDesc.textColor = .black
    }
    func textViewDidEndEditing(_ textView: UITextView) {
        if txtViewDesc.text.isEmpty
        {
            txtViewDesc.text = "Word Limit 250 max"
            txtViewDesc.textColor = .lightGray

        }
    }

    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if txtViewDesc.text.isEmpty
        {
            txtViewDesc.text = "Word Limit 250 max"
            txtViewDesc.textColor = .lightGray
        }
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
