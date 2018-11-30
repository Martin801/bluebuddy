//
//  ProfileViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/21/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import SafariServices
import MarqueeLabel
import NVActivityIndicatorView
class ProfileViewController: UIViewController, UITableViewDelegate, UITableViewDataSource , SFSafariViewControllerDelegate{

    @IBOutlet weak var lablChat: UILabel!
    @IBOutlet weak var imgChat: UIImageView!
    @IBOutlet weak var btnChat: UIButton!
    @IBOutlet weak var btnBuddylist: UIButton!
    @IBOutlet weak var btnTwitter: UIButton!
    @IBOutlet weak var btnInstaGram: UIButton!
    @IBOutlet weak var btnFb: UIButton!
    @IBOutlet weak var btnYouTube: UIButton!
    @IBOutlet weak var btnFreeDiving: UIButton!
    @IBOutlet weak var btnScuba: UIButton!
    @IBOutlet weak var img1: UIImageView!
    @IBOutlet weak var btnAddBuddy: UIButton!
    @IBOutlet weak var viewTbl: UIView!
    @IBOutlet weak var lblNoData: UILabel!
    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var txtAbtMe: UITextView!
    @IBOutlet weak var btnTrips1: UIButton!
    @IBOutlet weak var lblLine4: UILabel!
    @IBOutlet weak var lblLine3: UILabel!
    @IBOutlet weak var btnAbtMe1: UIButton!
    @IBOutlet weak var imgProfile: UIImageView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var lblAddress: MarqueeLabel!
    @IBOutlet weak var lblBuddyCount: UILabel!
    @IBOutlet weak var scrollViewBtm: UIScrollView!
    @IBOutlet weak var selectionview2: UIView!
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var lblLine2: UILabel!
    @IBOutlet weak var btnTrip: UIButton!
    @IBOutlet weak var lblLine1: UILabel!
    @IBOutlet weak var btnAbtMe: UIButton!
    @IBOutlet weak var selectionView1: UIView!
    @IBOutlet weak var lblBadgeCnt: UILabel!
    @IBOutlet weak var lblHeaderTitle: UILabel!
    @IBOutlet weak var lblWebsite: MarqueeLabel!
    var objProfile = ProfileBO()
    var prototypeCell = MyTripsTableViewCell()
    override func viewDidLoad() {
        super.viewDidLoad()

        lblLine2.isHidden = true
        lblLine3.isHidden = false
        lblLine4.isHidden = true
        btnTrips1.titleLabel?.textColor = .darkGray
        btnTrip.titleLabel?.textColor = .darkGray
        lblLine1.isHidden = false
        lblBuddyCount.layer.cornerRadius = lblBuddyCount.frame.size.height/2
        lblBuddyCount.clipsToBounds = true
        lblBadgeCnt.layer.cornerRadius = lblBadgeCnt.frame.size.height/2
        lblBadgeCnt.clipsToBounds = true
        lblNoData.isHidden = true
        txtAbtMe.text = objProfile.about
        lblName.text = objProfile.first_name.capitalized + " " + objProfile.last_name.capitalized
        lblAddress.text = objProfile.location
        lblWebsite.text = objProfile.website_link
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.tapFunction))
        lblWebsite.isUserInteractionEnabled = true
        lblWebsite.addGestureRecognizer(tap)
        
        lblBuddyCount.text = objProfile.buddy_counter
        img1.isHidden = true
        if objProfile.userId == LOGIN_CONSTANT.objProfile.userId
        {
            btnBuddylist.isHidden = false
            imgChat.isHidden = true
            btnChat.isHidden = true
            lablChat.isHidden = true
        }
        else 
        {
            btnBuddylist.isHidden = true
            imgChat.isHidden = false
            btnChat.isHidden = false
            lablChat.isHidden = false
        }
        let url = URL(string: objProfile.profile_pic)
        imgProfile.kf.setImage(with: url, placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        imgProfile.layer.cornerRadius = imgProfile.frame.size.height/2
        imgProfile.clipsToBounds = true
        
        if objProfile.isBuddy == "1"
        {
            btnAddBuddy.isHidden = false
            btnAddBuddy.setTitle("BUDDY", for: .normal)
            btnAddBuddy.backgroundColor = .lightGray
            btnAddBuddy.isEnabled = false
            btnAddBuddy.layer.cornerRadius = 5
        }
        else if objProfile.isBuddy == "0"
        {
            btnAddBuddy.setTitle("REQUEST SENT", for: .normal)
            btnAddBuddy.setImage(UIImage(named: ""), for: .normal)
            btnAddBuddy.backgroundColor = .lightGray
            btnAddBuddy.isEnabled = false
            btnAddBuddy.layer.cornerRadius = 5

        }
        else if objProfile.isBuddy == "3"
        {
            btnAddBuddy.isHidden = false
        }
        if objProfile.arrTrips.count == 0
        {
            lblNoData.isHidden = false
            lblNoData.text = "No trip found!"
            tblViewList.isHidden = true
        }
        else{
            lblNoData.isHidden = true
            tblViewList.isHidden = false
        }

        if objProfile.arrCertificate.count == 0
        {
            btnScuba.isHidden = true
            btnFreeDiving.isHidden = true
        }
        else if objProfile.arrCertificate.count == 1
        {
            btnScuba.isHidden = true
            btnFreeDiving.isHidden = true
            let value: CertificateBO = objProfile.arrCertificate[0] as! CertificateBO
            if value.Cert_type == "Free_Diving"
            {
                btnFreeDiving.isHidden = false
                btnFreeDiving.frame = CGRect(x: 5, y: 40, width: btnFreeDiving.frame.size.width, height: btnFreeDiving.frame.size.height)
                btnScuba.isHidden = true
            }
            else if value.Cert_type == "Scuba_Diving"
            {
                btnFreeDiving.isHidden = true
                btnScuba.frame = CGRect(x: 5, y: 40, width: btnScuba.frame.size.width, height: btnScuba.frame.size.height)
                btnScuba.isHidden = false
            }
        }
        else if objProfile.arrCertificate.count == 2
        {
            btnScuba.isHidden = false
            btnFreeDiving.isHidden = false
            btnScuba.frame = CGRect(x: 5, y: 5, width: btnScuba.frame.size.width, height: btnScuba.frame.size.height)
            btnFreeDiving.frame = CGRect(x: 5, y: btnScuba.frame.maxY + 10, width: btnFreeDiving.frame.size.width, height: btnFreeDiving.frame.size.height)
        }
        else{
            
        }
        if objProfile.arrSocialLink.count == 0
        {
            btnFb.isHidden = true
            btnInstaGram.isHidden = true
            btnTwitter.isHidden = true
            btnYouTube.isHidden = true
        }
        else if (objProfile.arrSocialLink.count == 1)
        {
            let val:Int = Int(objProfile.arrSocialLink[0] as! String)!
            switch val{
            case 1:
                btnFb.isHidden = false
                btnInstaGram.isHidden = true
                btnTwitter.isHidden = true
                btnYouTube.isHidden = true
                btnFb.frame = CGRect(x: 5, y: viewHeader.frame.size.height/2 + (btnFb.frame.size.height/2 ), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
            case 2:
                btnFb.isHidden = true
                btnInstaGram.isHidden = true
                btnTwitter.isHidden = false
                btnTwitter.frame = CGRect(x: 5, y: viewHeader.frame.size.height/2 + (btnFb.frame.size.height/2 ), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
                btnYouTube.isHidden = true
            case 3:
                btnFb.isHidden = true
                btnInstaGram.isHidden = true
                btnTwitter.isHidden = true
                btnYouTube.isHidden = false
                btnYouTube.frame = CGRect(x: 5, y: viewHeader.frame.size.height/2 + (btnFb.frame.size.height/2 ), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
            case 4:
                btnFb.isHidden = true
                btnInstaGram.isHidden = false
                btnTwitter.isHidden = true
                btnYouTube.isHidden = true
                btnInstaGram.frame = CGRect(x: 5, y: viewHeader.frame.size.height/2 + (btnFb.frame.size.height/2 ), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
                
            default:
                btnFb.isHidden = true
                btnInstaGram.isHidden = true
                btnTwitter.isHidden = true
                btnYouTube.isHidden = true
            }
        }
        else if(objProfile.arrSocialLink.count == 2)
        {
            btnFb.isHidden = true
            btnInstaGram.isHidden = true
            btnTwitter.isHidden = true
            btnYouTube.isHidden = true
            if objProfile.arrSocialLink.contains("1")
            {
                btnFb.isHidden = false
                btnFb.frame = CGRect(x: 5, y: viewHeader.frame.size.height - (btnFb.frame.size.height + 10) , width: btnFb.frame.size.width, height: btnFb.frame.size.height)
                if objProfile.arrSocialLink.contains("2")
                {
                    btnTwitter.isHidden = false
                    btnTwitter.frame = CGRect(x: 5, y: btnFb.frame.origin.y - (btnFb.frame.size.height + 10) , width: btnFb.frame.size.width, height: btnFb.frame.size.height)
                }
                else if objProfile.arrSocialLink.contains("3")
                {
                    btnYouTube.isHidden = false
                    btnYouTube.frame = CGRect(x: 5, y: btnFb.frame.origin.y - (btnFb.frame.size.height + 10), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
                }
                else if objProfile.arrSocialLink.contains("4")
                {
                    btnInstaGram.isHidden = false
                    btnInstaGram.frame = CGRect(x: 5, y: btnFb.frame.origin.y - (btnFb.frame.size.height + 10), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
                }
                else{
                    
                }
            }
            else if objProfile.arrSocialLink.contains("2")
            {
                btnTwitter.isHidden = false
                btnTwitter.frame = CGRect(x: 5, y: viewHeader.frame.size.height - (btnTwitter.frame.size.height + 10), width: btnTwitter.frame.size.width, height: btnTwitter.frame.size.height)
                if objProfile.arrSocialLink.contains("3")
                {
                    btnYouTube.isHidden = false
                    btnYouTube.frame = CGRect(x: 5, y: btnTwitter.frame.origin.y - (btnTwitter.frame.size.height + 10), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
                }
                else if objProfile.arrSocialLink.contains("4")
                {
                    btnInstaGram.isHidden = false
                    btnInstaGram.frame = CGRect(x: 5, y: btnTwitter.frame.origin.y - (btnTwitter.frame.size.height + 10), width: btnTwitter.frame.size.width, height: btnTwitter.frame.size.height)
                }
                else{
                    
                }
            }
            else if objProfile.arrSocialLink.contains("3")
            {
                btnYouTube.isHidden = false
                btnYouTube.frame = CGRect(x: 5, y: viewHeader.frame.size.height - (btnYouTube.frame.size.height + 10), width: btnYouTube.frame.size.width, height: btnYouTube.frame.size.height)
                if objProfile.arrSocialLink.contains("4")
                {
                    btnInstaGram.isHidden = false
                    btnInstaGram.frame = CGRect(x: 5, y: btnYouTube.frame.origin.y - (btnYouTube.frame.size.height + 10), width: btnYouTube.frame.size.width, height: btnYouTube.frame.size.height)
                }
                else{
                    
                }
            }
        }
            
        else if(objProfile.arrSocialLink.count == 3)
        {
            btnFb.isHidden = true
            btnInstaGram.isHidden = true
            btnTwitter.isHidden = true
            btnYouTube.isHidden = true
            if objProfile.arrSocialLink.contains("1")
            {
                btnFb.isHidden = false
                btnFb.frame = CGRect(x: 5, y: viewHeader.frame.size.height - (btnFb.frame.size.height + 10), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
                if objProfile.arrSocialLink.contains("2")
                {
                    btnTwitter.isHidden = false
                    btnTwitter.frame = CGRect(x: 5, y: btnFb.frame.origin.y - (btnTwitter.frame.size.height + 10), width: btnTwitter.frame.size.width, height: btnTwitter.frame.size.height)
                    if objProfile.arrSocialLink.contains("3")
                    {
                        btnYouTube.isHidden = false
                        btnYouTube.frame = CGRect(x: 5, y: btnTwitter.frame.origin.y - (btnTwitter.frame.size.height + 10), width: btnYouTube.frame.size.width, height: btnYouTube.frame.size.height)
                    }
                    else if objProfile.arrSocialLink.contains("4")
                    {
                        btnInstaGram.isHidden = false
                        btnInstaGram.frame = CGRect(x: 5, y: btnTwitter.frame.origin.y - (btnTwitter.frame.size.height + 10), width: btnInstaGram.frame.size.width, height: btnInstaGram.frame.size.height)
                    }
                    else{
                        
                    }
                }
                else if objProfile.arrSocialLink.contains("3")
                {
                    btnYouTube.isHidden = false
                    btnYouTube.frame = CGRect(x: 5, y: btnFb.frame.origin.y - (btnYouTube.frame.size.height + 10), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
                    if objProfile.arrSocialLink.contains("4")
                    {
                        btnInstaGram.isHidden = false
                        btnInstaGram.frame = CGRect(x: 5, y: btnYouTube.frame.origin.y - (btnInstaGram.frame.size.height + 10), width: btnInstaGram.frame.size.width, height: btnInstaGram.frame.size.height)
                    }
                    else{
                        
                    }
                }
                else{
                    
                }
            }
            else if objProfile.arrSocialLink.contains("2")
            {
                btnTwitter.isHidden = false
                btnTwitter.frame = CGRect(x: 5, y: viewHeader.frame.size.height - (btnTwitter.frame.size.height + 10), width: btnTwitter.frame.size.width, height: btnTwitter.frame.size.height)
                if objProfile.arrSocialLink.contains("3")
                {
                    btnYouTube.isHidden = false
                    btnYouTube.frame = CGRect(x: 5, y: btnTwitter.frame.origin.y - (btnYouTube.frame.size.height + 10), width: btnYouTube.frame.size.width, height: btnYouTube.frame.size.height)
                    if objProfile.arrSocialLink.contains("4")
                    {
                        btnInstaGram.isHidden = false
                        btnInstaGram.frame = CGRect(x: 5, y: btnYouTube.frame.origin.y - (btnInstaGram.frame.size.height + 10), width: btnInstaGram.frame.size.width, height: btnInstaGram.frame.size.height)
                    }
                    else{
                        
                    }
                }
                else{
                    
                }
            }
        }
        else if(objProfile.arrSocialLink.count == 4)
        {
            btnFb.isHidden = false
            btnInstaGram.isHidden = false
            btnTwitter.isHidden = false
            btnYouTube.isHidden = false
            btnFb.frame = CGRect(x: 5, y: viewHeader.frame.size.height - (btnFb.frame.size.height + 5), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
            btnTwitter.frame = CGRect(x: 5, y: btnFb.frame.origin.y - (btnTwitter.frame.size.height + 5), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
            btnYouTube.frame = CGRect(x: 5, y: btnTwitter.frame.origin.y - (btnTwitter.frame.size.height + 5), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
            btnInstaGram.frame = CGRect(x: 5, y: btnYouTube.frame.origin.y - (btnTwitter.frame.size.height + 5), width: btnFb.frame.size.width, height: btnFb.frame.size.height)
        }
        else{
            
        }
        
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblBadgeCnt.text = strCounter
                    }
                }
            }
        }

        self.scrollViewBtm.delaysContentTouches = true;
        self.scrollViewBtm.canCancelContentTouches = false;
        self.scrollViewBtm.panGestureRecognizer.delaysTouchesBegan = true;
        
        self.scrollViewMain.delaysContentTouches = true;
        self.scrollViewMain.canCancelContentTouches = false;
        self.scrollViewMain.panGestureRecognizer.delaysTouchesBegan = true;
                // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @objc
    func tapFunction(sender:UITapGestureRecognizer) {
        let url : NSURL = NSURL(string: lblWebsite.text!)!
        if UIApplication.shared.canOpenURL(url as URL) {
            UIApplication.shared.open(url as URL, options: [:]) { (boolValue) in
                
            }
        }
        else
        {
            Common.showAlert(message: "Sorry, couldn't open link", title: "BLUE BUDDY", viewController: self)
        }
    }
    
    @IBAction func onClickBtnSocialMedia(_ sender: UIButton) {
        
        var url = String()
        
        switch sender.tag {
        case 1:
            url = objProfile.fb_url
        case 2:
            url = objProfile.twtr_url
            
        case 3:
            url = objProfile.ggle_url
            
        case 4:
            url = objProfile.ingm_url
            
        default:
            break
        }
        let svc = SFSafariViewController(url: NSURL(string: url)! as URL)
        self.present(svc, animated: true, completion: nil)
        

    }
    @IBAction func onClickBtnCert(_ sender: UIButton) {
        
        switch sender.tag  {
        case 1:
            let certificationDetailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationDetailsViewController") as? CertificationDetailsViewController
            certificationDetailsVC?.strSelectedItem = 1
            certificationDetailsVC?.fromVC = 3

            if objProfile.arrCertificate.count == 1
            {
                let value: CertificateBO = objProfile.arrCertificate[0] as! CertificateBO
                if value.Cert_type == "Scuba_Diving"
                {
                    certificationDetailsVC?.objCert = value
                    
                }
            }
            else{
                let value: CertificateBO = objProfile.arrCertificate[0] as! CertificateBO
                let value1: CertificateBO = objProfile.arrCertificate[1] as! CertificateBO
                if value.Cert_type == "Scuba_Diving"
                {
                    certificationDetailsVC?.objCert = value
                    
                }
                else if value1.Cert_type == "Scuba_Diving"
                {
                    certificationDetailsVC?.objCert = value1
                    
                }
            }
            
            self.navigationController?.pushViewController(certificationDetailsVC!, animated: true)
            
        case 2:
            
            let freeDiverVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "FreeDiverCertificationDetailsViewController") as! FreeDiverCertificationDetailsViewController
            freeDiverVC.fromVC = 3
            
            if objProfile.arrCertificate.count == 1
            {
                let value: CertificateBO = objProfile.arrCertificate[0] as! CertificateBO
                if value.Cert_type == "Free_Diving"
                {
                    freeDiverVC.objCert = value
                    
                }
            }
            else{
                let value: CertificateBO = objProfile.arrCertificate[0] as! CertificateBO
                let value1: CertificateBO = objProfile.arrCertificate[1] as! CertificateBO
                if value.Cert_type == "Free_Diving"
                {
                    freeDiverVC.objCert = value
                    
                }
                else if value1.Cert_type == "Free_Diving"
                {
                    freeDiverVC.objCert = value1
                    
                }
            }

            self.navigationController?.pushViewController(freeDiverVC, animated: true)
        default:
            break
        }

    }
    @IBAction func onClickBtnAddBuddy(_ sender: UIButton) {
        
        let dictVal = NSMutableDictionary()
        dictVal["buddy_id"] = objProfile.userId
        dictVal["device_type"] = "ios"
        UserParser.callAddBuddyService(dictVal, complete: { (status, strMsg) in
            if status
            {
                DispatchQueue.main.async {
                    self.btnAddBuddy.isHidden = true
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            }
            else{
                DispatchQueue.main.async {
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            }
        })

    }
    @IBAction func onClickBtnSelection(_ sender: UIButton) {
        if  sender.tag == 1 {
            lblLine2.isHidden = true
            lblLine1.isHidden = false
            btnTrip.titleLabel?.textColor = .darkGray
            lblLine3.isHidden = false
            lblLine4.isHidden = true
            btnTrips1.titleLabel?.textColor = .darkGray
            
        }
        else{
            lblLine1.isHidden = true
            lblLine2.isHidden = false
            btnAbtMe.titleLabel?.textColor = .darkGray
            lblLine3.isHidden = true
            lblLine4.isHidden = false
            btnAbtMe1.titleLabel?.textColor = .darkGray
        }
        let pageWidth:CGFloat = scrollViewBtm.frame.width
        let contentOffset:CGFloat = scrollViewBtm.contentOffset.x
        
        var slideToX = contentOffset + pageWidth
        
        if  sender.tag == 1
        {
            slideToX = 0
        }
        else{
            slideToX = contentOffset + pageWidth
        }
        scrollViewBtm.scrollRectToVisible(CGRect(x:slideToX, y:0, width:pageWidth, height:scrollViewBtm.frame.height), animated: true)
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }

    @IBAction func onClickBtnNotifications(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    
    @IBAction func onClickBtnBuddyList(_ sender: UIButton) {
        UserParser.callBuddyListService("", complete: { (status, strMsg, arrList) in
            if status
            {
                DispatchQueue.main.async {
                    let buddyVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "BuddyListViewController") as! BuddyListViewController
                    buddyVC.arrBuddies = arrList
                    self.navigationController?.pushViewController(buddyVC, animated: true)
                }
            }
            else{
                DispatchQueue.main.async {
                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                }
            }
        })
    }
    
    @IBAction func onClickBtnChat(_ sender: UIButton) {
        ProfileViewController.callChatDetails(objProfile.firebase_id, self)
    }
    // MARK: -
    // MARK: - UitableViewDelegates & Datasources
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
            self.prototypeCell = tblViewList.dequeueReusableCell(withIdentifier: "myTrips_Cell")! as! MyTripsTableViewCell
            let objVal:TripsDetailsBO = objProfile.arrTrips[indexPath.row] as! TripsDetailsBO
            let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: prototypeCell.lblDesc.frame.size.width, font: prototypeCell.lblDesc.font, string: objVal.trip_desc)
            objVal.cellHgt = cellHgt + 25
            return 237 + cellHgt
       
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
            return objProfile.arrTrips.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
            let cell = tableView.dequeueReusableCell(withIdentifier: "myTrips_Cell", for: indexPath) as! MyTripsTableViewCell
            let objTrip: TripsDetailsBO = objProfile.arrTrips[indexPath.row] as! TripsDetailsBO
            cell.imgBg.layer.borderWidth = 0.5
            cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
            cell.imgBg.layer.cornerRadius = 8.0
            cell.imgBg.layer.shadowColor = UIColor.lightGray.cgColor
            
            cell.lblNo.layer.cornerRadius = cell.lblNo.frame.size.width/2
            cell.lblNo.clipsToBounds = true
            
            cell.lblName.text = objTrip.activity
            cell.lblDate.text = objTrip.event_date
            cell.lblDesc.text = objTrip.trip_desc
            cell.lblAddress.text = objTrip.event_loc
        
        
        if !objTrip.is_editable && objTrip.is_participated
        {
            cell.btnEdit.isHidden = false
//            cell.btnEdit.setTitle("REQUEST SENT", for: .normal)
//            cell.btnEdit.isEnabled = false
            
            cell.btnEdit.setTitle("UNREQUEST", for: .normal)
            cell.btnEdit.isEnabled = true
        }
        else
        {
            if objTrip.is_editable
            {
                cell.btnEdit.isHidden = false
                cell.btnEdit.setTitle("EDIT", for: .normal)
                cell.btnEdit.isEnabled = true
            }
            else
            {
                cell.btnEdit.isHidden = false
                cell.btnEdit.isEnabled = true
                cell.btnEdit.setTitle("I'M IN", for: .normal)
                cell.btnEdit.setBackgroundImage(#imageLiteral(resourceName: "Rectangle 90.png"), for: .normal)
            }
        }
        
            print(objTrip.cellHgt)
            //   let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: cell.lblDesc.frame.width, font: cell.lblDesc.font!, string: objTrip.trip_desc) + 25
            cell.lblDescHgtConstraint.constant = CGFloat(objTrip.cellHgt)
            
            cell.onClickDetails = {() -> Void in
                TripsParser.callTripsDetailsService(["event_id" : objTrip.event_id], complete: { (status, strMsg, objValue, objTripData) in
                    if status
                    {
                        DispatchQueue.main.async {
                            //                        let myViewController = DetailsViewController(nibName: "DetailsViewController", bundle: nil)
                            //                        self.navigationController?.pushViewController(myViewController, animated: true)
                            let tripDetailsVC = self.storyboard!.instantiateViewController(withIdentifier: "TripDetailsViewController") as! TripDetailsViewController
                            tripDetailsVC.objTripDetail = objTrip
                            tripDetailsVC.arrParticipants = objValue as! NSMutableArray
                            self.navigationController?.pushViewController(tripDetailsVC, animated: true)
                        }
                    }
                    else
                    {
                        DispatchQueue.main.async {
                            Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                        }
                    }
                })
            }
            cell.onClickEdit = {() -> Void in
                
                if objTrip.is_editable
                {
                    let editVC = self.storyboard!.instantiateViewController(withIdentifier: "CreateTripViewController") as! CreateTripViewController
                    editVC.objTrips = objTrip
                    editVC.isFromUpdate = true
                    editVC.onClickDelete = {() -> Void in
                        self.objProfile.arrTrips.remove(objTrip)
                        self.tblViewList.reloadData()
                    }
                    editVC.onClickEdit = {(objValue) -> Void in
                        self.objProfile.arrTrips.remove(objTrip)
                        self.objProfile.arrTrips.add(objValue)
                        self.tblViewList.reloadData()
                    }
                    self.navigationController?.pushViewController(editVC, animated: true)

                }
                else if objTrip.is_participated {
                    
                    
                    let alert = UIAlertController(title: "BLUE BUDDY",
                                                  message: "Are you sure you want to remove your participation?",
                                                  preferredStyle: UIAlertControllerStyle.alert)
                    let settingAction = UIAlertAction(title: "Yes" , style: .default, handler: {(_ action: UIAlertAction) -> Void in
                        
                        
                        TripsParser.callDeleteParticipationInTripsService(["event_id" : objTrip.event_id, "device_type" : "ios", "participant_id" :LOGIN_CONSTANT.objProfile.userId], complete: { (status, strMsg) in
                            if status
                            {
                                DispatchQueue.main.async {
                                    //                            cell.btnEdit.setTitle("REQUEST SENT", for: .normal)
                                    cell.btnEdit.setTitle("I'M IN", for: .normal)
                                    cell.btnEdit.isEnabled = true
                                    objTrip.is_participated = false
                                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                                }
                            }
                            else
                            {
                                DispatchQueue.main.async {
                                    Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                                }
                            }
                        })
                        
                        
                    })
                    
                    let cancelAction = UIAlertAction(title: "No", style: .cancel, handler: nil)
                    
                    alert.addAction(settingAction)
                    alert.addAction(cancelAction)
                    self.present(alert, animated: true, completion: nil)
                    
                    
                    
                }
                    
                else{
                    TripsParser.callParticipateInTripsService(["event_id" : objTrip.event_id, "device_type" : "ios"], complete: { (status, strMsg) in
                        if status
                        {
                            DispatchQueue.main.async {
//                                cell.btnEdit.setTitle("REQUEST SENT", for: .normal)
//                                cell.btnEdit.isEnabled = false
                                cell.btnEdit.setTitle("UNREQUEST", for: .normal)
                                cell.btnEdit.isEnabled = true
                                objTrip.is_participated = true
                                Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                            }
                        }
                        else
                        {
                            DispatchQueue.main.async {
                                Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                            }
                        }
                    })
                }
                
            }
            return cell
        
    }
    
    // MARK: -
    // MARK: - ScrollView Delegates
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollViewMain.contentOffset.y >= viewHeader.frame.size.height
        {
            selectionView1.isHidden = true
            self.view.bringSubview(toFront: selectionView1)
            lblHeaderTitle.text = objProfile.first_name.capitalized + " " + objProfile.last_name.capitalized
            scrollViewMain.isScrollEnabled = false
            scrollViewMain.setContentOffset(CGPoint(x: 0,y: viewHeader.frame.size.height), animated: true)
            scrollViewMain.isScrollEnabled = true
            
            if scrollView.contentOffset.y > viewHeader.frame.size.height {
                scrollView.isScrollEnabled = false
            } else if scrollView.contentOffset.y < viewHeader.frame.size.height {
                scrollView.isScrollEnabled = false
            }
            
            self.tblViewList.isScrollEnabled = true
        }
        else{
//            self.tblViewList.isScrollEnabled  = false
            selectionView1.isHidden = true
            lblHeaderTitle.text = "PROFILE"
        }
    }
    
    func scrollViewWillEndDragging(_ scrollView: UIScrollView, withVelocity velocity: CGPoint, targetContentOffset: UnsafeMutablePointer<CGPoint>) {
        
        if targetContentOffset.pointee.y < scrollView.contentOffset.y {
            print("it's going up")
            // it's going up
        } else {
            print("it's going down")
            // it's going down
        }
    }

    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView){
        // Test the offset and calculate the current page after scrolling ends
        
        if scrollView == scrollViewBtm
        {
            let pageWidth:CGFloat = scrollView.frame.width
            let currentPage:CGFloat = floor((scrollView.contentOffset.x-pageWidth/2)/pageWidth)+1
            if  Int(currentPage) == 0 {
                lblLine2.isHidden = true
                lblLine1.isHidden = false
                lblLine3.isHidden = false
                lblLine4.isHidden = true
                btnTrips1.titleLabel?.textColor = .darkGray
                btnTrip.titleLabel?.textColor = .darkGray
            }
            else{
                lblLine1.isHidden = true
                lblLine2.isHidden = false
                btnAbtMe1.titleLabel?.textColor = .darkGray
                lblLine3.isHidden = true
                lblLine4.isHidden = false
                btnAbtMe.titleLabel?.textColor = .darkGray
            }
        }
    }
    
    // MARK: -
    // MARK: - SafariViewController Delegates
    func safariViewControllerDidFinish(_ controller: SFSafariViewController)
    {
        controller.dismiss(animated: true, completion: nil)
    }
    
    class func callChatDetails( _ strId: String, _ targetVc: UIViewController) {
        let activityData = ActivityData()
        NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
            NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
        }
        User.info(forUserID: strId, completion: { (user) in
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
            }
            DispatchQueue.main.async {
                let chatVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
                chatVC.objUser = user
                targetVc.present(chatVC, animated: true, completion: nil)
            }
        })
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
