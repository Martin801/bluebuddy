//
//  FreeDiverCertificationDetailsViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/24/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class FreeDiverCertificationDetailsViewController: UIViewController{

    @IBOutlet weak var btnSub1: UIButton!
    @IBOutlet weak var btnSub: UIButton!
    @IBOutlet weak var btnAdd: UIButton!
    @IBOutlet weak var btnSkip: UIButton!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var btnLogOut: UIButton!
    @IBOutlet weak var imgLogOut: UIImageView!
    @IBOutlet weak var view2HgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var view1HgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var txtLevel2: UITextField!
    @IBOutlet weak var txtLevel1: UITextField!
    @IBOutlet weak var viewLevel2: UIView!
    @IBOutlet weak var viewLevel1: UIView!
    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var txtCertificationNo: UITextField!
    @IBOutlet weak var txtCertification: UITextField!
    @IBOutlet weak var txtAgencyName: UITextField!

    @IBOutlet weak var lblEnterAgencyNameDesc: UILabel!
    @IBOutlet weak var lblEnterCertLevelDesc: UILabel!
    @IBOutlet weak var lblAddCertNumber: UILabel!
    @IBOutlet weak var lblEnterCertLevel: UILabel!
    
    var fromVC = Int()
    var objCert = CertificateBO()
    override func viewDidLoad() {
        super.viewDidLoad()
        viewLevel1.isHidden = true
        viewLevel2.isHidden = true
        if fromVC == 1 || fromVC == 2 || fromVC == 3
        {
            imgLogOut.isHidden = true
            btnLogOut.isHidden = true
        }
        else
        {
            imgLogOut.isHidden = false
            btnLogOut.isHidden = false
        }
        if fromVC == 2
        {
            btnNext.setTitle("UPDATE", for: .normal)
            btnSkip.setTitle("DELETE", for: .normal)
            txtAgencyName.text = objCert.Cert_name
            if !objCert.Cert_no.isEmpty
            {
                txtCertificationNo.text = objCert.Cert_no
            }
            if objCert.arrLevel.count == 2
            {
                txtCertification.text = objCert.Cert_level
                viewLevel1.isHidden = false
                view1HgtConstraint.constant = 45
                txtLevel1.text = objCert.Cert_level2
            }
            else if (objCert.arrLevel.count == 3)
            {
                txtCertification.text = objCert.Cert_level
                viewLevel1.isHidden = false
                view1HgtConstraint.constant = 45
                viewLevel2.isHidden = false
                view2HgtConstraint.constant = 45
                txtLevel1.text = objCert.Cert_level2
                txtLevel2.text = objCert.Cert_level3
            }
            else
            {
                txtCertification.text = objCert.Cert_level
            }

        }
        else if fromVC == 3
        {
            btnNext.isHidden = true
            btnSkip.isHidden = true
            txtLevel1.isEnabled = false
            txtLevel2.isEnabled = false
            txtAgencyName.isEnabled = false
            txtCertification.isEnabled = false
            txtCertificationNo.isEnabled = false
            btnAdd.isHidden = true
            btnSub.isHidden = true
            btnSub1.isHidden = true
            txtAgencyName.text = objCert.Cert_name
            if !objCert.Cert_no.isEmpty
            {
                txtCertificationNo.text = objCert.Cert_no
            }
            if objCert.arrLevel.count == 2
            {
                txtCertification.text = objCert.Cert_level
                viewLevel1.isHidden = false
                view1HgtConstraint.constant = 45
                txtLevel1.text = objCert.Cert_level2
            }
            else if (objCert.arrLevel.count == 3)
            {
                txtCertification.text = objCert.Cert_level
                viewLevel1.isHidden = false
                view1HgtConstraint.constant = 45
                viewLevel2.isHidden = false
                view2HgtConstraint.constant = 45
                txtLevel1.text = objCert.Cert_level2
                txtLevel2.text = objCert.Cert_level3
            }
            else
            {
                txtCertification.text = objCert.Cert_level
            }
            
            displayCertificatesOnly()
        }

        

        // Do any additional setup after loading the view.
    }
    
    func displayCertificatesOnly(){
        lblEnterCertLevelDesc.isHidden = true
        lblEnterAgencyNameDesc.isHidden = true
        lblEnterCertLevel.text = "Certification Level"
        lblAddCertNumber.text = "Certification Number"
        txtCertificationNo.placeholder = ""
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    // MARK: -
    // MARK: - UIButton Action Methods
    
    @IBAction func onclickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickBtnSub(_ sender: UIButton) {
        
        switch sender.tag {
        case 1:
            view1HgtConstraint.constant = 0
            viewLevel1.isHidden = true
            txtLevel1.text = nil
        case 2:
            view2HgtConstraint.constant = 0
            viewLevel2.isHidden = true
            txtLevel2.text = nil
        default:
            break
        }
    }
    
    @IBAction func onClickBtnSkip(_ sender: UIButton) {
        if self.fromVC == 1
        {
            self.callProfileService()
        }
        else if (self.fromVC == 2)
        {
            DispatchQueue.main.async {
                let alert = UIAlertController(title: "BLUE BUDDY", message: "Do you want to delete this certification?", preferredStyle: UIAlertControllerStyle.alert)
                let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                    DispatchQueue.main.async {
                        let dictVal = NSMutableDictionary()
                        dictVal["type"] = "certification"
                        dictVal["data_id"] = self.objCert.Cert_id
                        TripsParser.callDeleteService(dictVal, complete: {(status, strMsg) in
                            if status
                            {
                                DispatchQueue.main.async {
                                    self.callProfileService()
                                }
                            }
                            else{
                                DispatchQueue.main.async {
                                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                                }
                            }
                        })
                    }
                })
                let cancelAction = UIAlertAction(title: NSLocalizedString("CANCEL", comment: "Cancel action"), style: .cancel, handler: nil)
                alert.addAction(okAction)
                alert.addAction(cancelAction)
                self.present(alert, animated: true, completion: nil)
            }
        }

        else
        {
            UserDefaults.standard.set("4", forKey:kPageNo)
            self.callAboutYourSelfVC()
        }
    }
    @IBAction func onClickBtnNext(_ sender: UIButton) {
        self.view.endEditing(true)
        if (txtAgencyName.text?.isEmpty)! {
            Common.showAlert(message: "Please enter the agency name", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtCertification.text?.isEmpty)! {
            Common.showAlert(message: "Please enter your certification", title: "BLUE BUDDY", viewController: self)
        }
        else{
            let dictVal = NSMutableDictionary()
            dictVal["cert_agency"] = txtAgencyName.text
            dictVal["cert_level"] = txtCertification.text
            dictVal["cert_level2"] = (txtLevel1.text?.isEmpty)! ? "" : txtLevel1.text
            dictVal["cert_level3"] = (txtLevel2.text?.isEmpty)! ? "" : txtLevel2.text
            dictVal["cert_no"] = (txtCertificationNo.text?.isEmpty)! ? "" : txtCertificationNo.text
            dictVal["cert_type"] = "Free_Diving"
            dictVal["next_step"]    = fromVC == 0 ? "4" : ""
            if fromVC == 0 {
                UserDefaults.standard.set("4", forKey:kPageNo)

            }
            UserParser.callUpdateCertificateService(dictVal, complete: { (status, strMsg) in
                if status
                {
                    DispatchQueue.main.async {
                        if self.fromVC == 1
                        {
                            self.callProfileService()
                        }
                        else if self.fromVC == 2
                        {
                            DispatchQueue.main.async {
                                let alert = UIAlertController(title: "BLUE BUDDY", message: strMsg, preferredStyle: UIAlertControllerStyle.alert)
                                let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                                    DispatchQueue.main.async {
                                        self.callProfileService()
                                    }
                                })
                                alert.addAction(okAction)
                                self.present(alert, animated: true, completion: nil)
                            }

                        }
                        else
                        {
                            self.callAboutYourSelfVC()
                        }
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

    @IBAction func onClickBtnAdd(_ sender: UIButton) {
        if view1HgtConstraint.constant == 0 {
            view1HgtConstraint.constant = 45
            viewLevel1.isHidden = false
        }
        else if (view1HgtConstraint.constant == 45)
        {
            view2HgtConstraint.constant = 45
            viewLevel2.isHidden = false
        }
        else{
            
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    func callAboutYourSelfVC()
    {
        let aboutVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AboutYourselfViewController") as! AboutYourselfViewController
        self.navigationController?.pushViewController(aboutVC, animated: true)
    }
    
    func callProfileService()
    {
        UserParser.callProfileDetailsService("") { (status, strMsg, objProfile) in
            if status
            {
                DispatchQueue.main.async {
                    var isFound:Bool = false
                    if let viewControllers = self.navigationController?.viewControllers {
                        for viewController in viewControllers {
                            // some process
                            if viewController.isKind(of: CertificateListViewController.self) {
                                print("yes it is")
                                isFound = true
                                self.navigationController?.popToViewController(viewController, animated: true)
                            }
                        }
                        if !isFound
                        {
                            let certVC:UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "CertificateListViewController") as! CertificateListViewController
                            self.navigationController?.pushViewController(certVC, animated: true)
                        }
                    }
                    
                }
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
