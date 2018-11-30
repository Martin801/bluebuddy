//
//  OtherLinkViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/3/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class OtherLinkViewController: UIViewController {

    @IBOutlet weak var txtWebsite: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()

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
    @IBAction func onClickBtnSkip(_ sender: UIButton) {
//        let dictVal = NSMutableDictionary()
//        dictVal["next_step"]    = "7"
        UserDefaults.standard.set("7", forKey:kPageNo)
        self.callLocationVC()
        
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }

    @IBAction func onClickBtnNext(_ sender: UIButton) {
        self.view.endEditing(true)
        if (txtWebsite.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your website", title: "BLUE BUDDY", viewController: self)
        }
        else
        {
            let dictVal = NSMutableDictionary()
            dictVal["website_link"] = txtWebsite.text
            dictVal["next_step"]    = "7"
            UserParser.callUpdateProfileService(dictVal) { (status, strMsg) in
                if status
                {
                    UserDefaults.standard.set("7", forKey:kPageNo)
                    self.callLocationVC()

                }
            }
        }
    }
    func callLocationVC()
    {
        DispatchQueue.main.async {
            let locationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "LocationViewController") as! LocationViewController
            self.navigationController?.pushViewController(locationVC, animated: true)
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
