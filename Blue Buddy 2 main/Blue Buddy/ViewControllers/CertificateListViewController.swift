//
//  CertificateListViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/20/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class CertificateListViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var btnAddMore: UIButton!
    @IBOutlet weak var btnCertification: UIButton!
    @IBOutlet weak var viewNoData: UIView!
    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var lblBadgeCount: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        self.view.bringSubview(toFront: btnAddMore)
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblBadgeCount.text = strCounter
                    }
                }
            }
        }

                // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if LOGIN_CONSTANT.objProfile.arrCertificate.count == 0{
            viewNoData.isHidden = false
            tblViewList.isHidden = true
            btnAddMore.isHidden = true
        }
        else{
            viewNoData.isHidden = true
            tblViewList.isHidden = false
            btnAddMore.isHidden = LOGIN_CONSTANT.objProfile.arrCertificate.count == 2 ? true : false
            tblViewList.reloadData()
        }
        
    }
    
    
    // MARK: -
    // MARK: - UIButton Actions
    @IBAction func onClickBtnAdd(_ sender: UIButton) {
        for i in 0..<LOGIN_CONSTANT.objProfile.arrCertificate.count
        {
            let objCer = LOGIN_CONSTANT.objProfile.arrCertificate[i] as! CertificateBO
            if objCer.Cert_type == "Scuba_Diving" {
                let freeDiverVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "FreeDiverCertificationDetailsViewController") as! FreeDiverCertificationDetailsViewController
                freeDiverVC.fromVC = 1
                self.navigationController?.pushViewController(freeDiverVC, animated: true)
            }
            else if (objCer.Cert_type == "Free_Diving")
            {
                let certificationDetailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationDetailsViewController") as? CertificationDetailsViewController
                certificationDetailsVC?.strSelectedItem = 1
                certificationDetailsVC?.fromVC = 1
                self.navigationController?.pushViewController(certificationDetailsVC!, animated: true)
            }
            else
            {
                
            }
        }
        
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }

    @IBAction func onClickbtnaddCert(_ sender: UIButton) {
        let certificationVC = self.storyboard!.instantiateViewController(withIdentifier: "CertificationViewController") as! CertificationViewController
        certificationVC.isFromProfile = true
        self.navigationController?.pushViewController(certificationVC, animated: true)
    }
    @IBAction func onClickBtnNotifications(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return LOGIN_CONSTANT.objProfile.arrCertificate.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "certificateListCell", for: indexPath) as! CertificateListTableViewCell
        let objCer = LOGIN_CONSTANT.objProfile.arrCertificate[indexPath.row] as! CertificateBO
        let type: Int = objCer.Cert_type == "Scuba_Diving" ? 1 : 2
        cell.lblName.text = type == 1 ? "Scuba Diving" : "Freediving"
        cell.lblAgencyName.text = objCer.Cert_name
        cell.lblCertNO.text = objCer.Cert_no
        cell.imgBg.layer.borderWidth = 0.5
        cell.lblLevel1.text = objCer.Cert_level
        if objCer.arrLevel.count == 2
        {
            cell.lblLevel2.text = objCer.Cert_level2
            cell.lblLevel2HgtConstraint.constant = 20
        }
        else if (objCer.arrLevel.count == 3)
        {
            cell.lblLevel2.text = objCer.Cert_level2
            cell.lblLevel2HgtConstraint.constant = 20
            cell.lblLevel3.text = objCer.Cert_level3
            cell.lbl3HgtConstraint.constant = 20
        }
        cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
        cell.imgBg.layer.cornerRadius = 8.0
        cell.imgBg.layer.shadowColor = UIColor.lightGray.cgColor
        cell.onClickEdit = {() -> Void in
            if type == 1
            {
                let certificationDetailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationDetailsViewController") as? CertificationDetailsViewController
                certificationDetailsVC?.strSelectedItem = 1
                certificationDetailsVC?.fromVC = 2
                certificationDetailsVC?.objCert = objCer
                self.navigationController?.pushViewController(certificationDetailsVC!, animated: true)
            }
            else{
                let freeDiverVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "FreeDiverCertificationDetailsViewController") as! FreeDiverCertificationDetailsViewController
                freeDiverVC.fromVC = 2
                freeDiverVC.objCert = objCer
                self.navigationController?.pushViewController(freeDiverVC, animated: true)
            }
        }
        cell.onClickDelete = {() -> Void in
            self.callDeleteCertification(objCer)
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let objCer = LOGIN_CONSTANT.objProfile.arrCertificate[indexPath.row] as! CertificateBO
        if objCer.arrLevel.count == 2
        {
            return 220
        }
        else if (objCer.arrLevel.count == 3)
        {
            return 240
        }
        else
        {
            return 198
        }
    }

    func callDeleteCertification( _ objCert: CertificateBO)
    {
        DispatchQueue.main.async {
            let alert = UIAlertController(title: "BLUE BUDDY", message: "Do you want to delete this certification?", preferredStyle: UIAlertControllerStyle.alert)
            let okAction = UIAlertAction(title: NSLocalizedString("OK", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                DispatchQueue.main.async {
                    let dictVal = NSMutableDictionary()
                    dictVal["type"] = "certification"
                    dictVal["data_id"] = objCert.Cert_id
                    TripsParser.callDeleteService(dictVal, complete: {(status, strMsg) in
                        if status
                        {
                            DispatchQueue.main.async {
                                LOGIN_CONSTANT.objProfile.arrCertificate.remove(objCert)
                                self.tblViewList.reloadData()
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
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
