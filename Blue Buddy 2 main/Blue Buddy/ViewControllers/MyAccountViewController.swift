//
//  MyAccountViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/9/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import MarqueeLabel
import SafariServices
class MyAccountViewController: UIViewController, UITableViewDelegate, UITableViewDataSource , UIScrollViewDelegate, SFSafariViewControllerDelegate{

    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var btnBuddyList: UIButton!
    @IBOutlet weak var btnFreeDrive: UIButton!
    @IBOutlet weak var btnScuba: UIButton!
    @IBOutlet weak var btnTwitter: UIButton!
    @IBOutlet weak var btnYouTube: UIButton!
    @IBOutlet weak var btnInstagram: UIButton!
    @IBOutlet weak var btnFB: UIButton!
    @IBOutlet weak var imgUser: UIImageView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var lblAddress: MarqueeLabel!
    @IBOutlet weak var viewBtm: UIView!
    @IBOutlet weak var btnBuddy: UIButton!
    @IBOutlet weak var lblNoData: UILabel!
    @IBOutlet weak var img1: UIImageView!
    @IBOutlet weak var tblTrip: UITableView!
    @IBOutlet weak var lblNotifications: UILabel!
    @IBOutlet weak var lblBuddiesNo: UILabel!
    @IBOutlet weak var btnAdd: UIButton!
    @IBOutlet weak var btnAbtUs1: UIButton!
    @IBOutlet weak var lblLine4: UILabel!
    @IBOutlet weak var lblLine3: UILabel!
    @IBOutlet weak var btnTrip1: UIButton!
    @IBOutlet weak var scrollViewBtm: UIScrollView!
    @IBOutlet weak var btnTrips: UIButton!
    @IBOutlet weak var btnAbout: UIButton!
    @IBOutlet weak var line2: UILabel!
    @IBOutlet weak var line1: UILabel!
    @IBOutlet weak var viewTrip: UIView!
    @IBOutlet weak var viewSwipe: UIView!
    @IBOutlet weak var txtaboutus: UITextView!
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var selectionView: UIView!
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var selectionList1: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet var viewTop: UIView!
    @IBOutlet weak var lblWebsite: MarqueeLabel!
    @IBOutlet weak var tblViewAccount: UITableView!
    var goingUp: Bool?
    var isScrollKilled: Bool?
    var childScrollingDownDueToParent = false
    var prototypeCell = MyTripsTableViewCell()
    let arrNames = ["Profile", "Edit Profile", "Certifications", "Trips", "Buddies", "My Listings", "Reviews", "Change password", "Logout"]
    let arrImages = ["footer-icon-profile.png", "edit1.png", "certificate1.png", "footer-icon-trip.png", "buddies1.png", "footer-icon-market.png", "reviews1.png", "lock1.png", "log out.png"]
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        [self .setupView()]
        let valu = UserDefaults.standard.bool(forKey: "isProfileOpen")
        if valu
        {
            tblViewAccount.isHidden = false
            scrollViewMain.isHidden = true
            btnBack.isHidden = false
        }
        else{
            tblViewAccount.isHidden = true
            scrollViewMain.isHidden = false
            btnBack.isHidden = true
            UserDefaults.standard.set(true, forKey: "isProfileOpen")
        }

        
        
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
    
    func setupView() {
        
        btnBuddy.isHidden = true
        img1.isHidden = true
        lblNoData.isHidden = true
        lblName.text = LOGIN_CONSTANT.objProfile.first_name.capitalized + " " + LOGIN_CONSTANT.objProfile.last_name.capitalized
        lblAddress.text = LOGIN_CONSTANT.objProfile.location
        lblWebsite.text = LOGIN_CONSTANT.objProfile.website_link
        
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.tapFunction))
        lblWebsite.isUserInteractionEnabled = true
        lblWebsite.addGestureRecognizer(tap)
        
        
        lblBuddiesNo.text = LOGIN_CONSTANT.objProfile.buddy_counter
        imgUser.layer.cornerRadius = imgUser.frame.size.width/2
        imgUser.clipsToBounds = true
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblNotifications.text = strCounter
                    }
                }
            }
        }
        let url = URL(string:LOGIN_CONSTANT.objProfile.profile_pic)
        imgUser.kf.setImage(with: url, placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        if LOGIN_CONSTANT.objProfile.buddy_counter == "0"
        {
            btnBuddyList.isHidden = true
        }
        else{
            btnBuddyList.isHidden = false
        }
        if LOGIN_CONSTANT.objProfile.arrTrips.count == 0
        {
            btnBuddy.isHidden = false
            img1.isHidden = false
            lblNoData.isHidden = false
            tblTrip.isHidden = true
        }
        else{
            btnBuddy.isHidden = true
            img1.isHidden = true
            lblNoData.isHidden = true
            tblTrip.isHidden = false
        }
//        UserDefaults.standard.set(true, forKey: "isProfileOpen")
        let valu = UserDefaults.standard.bool(forKey: "isProfileOpen")
        selectionList1.isHidden = true
        if valu
        {
            tblViewAccount.isHidden = false
            scrollViewMain.isHidden = true
        }
        else{
            tblViewAccount.isHidden = true
            scrollViewMain.isHidden = false
        }
        if LOGIN_CONSTANT.objProfile.arrCertificate.count == 0
        {
            btnScuba.isHidden = true
            btnFreeDrive.isHidden = true
        }
        else if LOGIN_CONSTANT.objProfile.arrCertificate.count == 1
        {
            btnScuba.isHidden = true
            btnFreeDrive.isHidden = true
            let value: CertificateBO = LOGIN_CONSTANT.objProfile.arrCertificate[0] as! CertificateBO
            if value.Cert_type == "Free_Diving"
            {
                btnFreeDrive.isHidden = false
                btnFreeDrive.frame = CGRect(x: 5, y: 40, width: btnFreeDrive.frame.size.width, height: btnFreeDrive.frame.size.height)
                btnScuba.isHidden = true
            }
            else if value.Cert_type == "Scuba_Diving"
            {
                btnFreeDrive.isHidden = true
                btnScuba.frame = CGRect(x: 5, y: 40, width: btnScuba.frame.size.width, height: btnScuba.frame.size.height)
                btnScuba.isHidden = false
            }
            
        }
        else if LOGIN_CONSTANT.objProfile.arrCertificate.count == 2
        {
            btnScuba.isHidden = false
            btnFreeDrive.isHidden = false
            btnScuba.frame = CGRect(x: 5, y: 40, width: btnScuba.frame.size.width, height: btnScuba.frame.size.height)
            btnFreeDrive.frame = CGRect(x: 5, y: btnScuba.frame.maxY + 10, width: btnFreeDrive.frame.size.width, height: btnFreeDrive.frame.size.height)
        }
        else{
            
        }
        btnFB.frame = CGRect(x: (self.view.frame.size.width - btnFB.frame.size.width)/2 , y: viewHeader.frame.size.height - (btnFB.frame.size.height + 15), width: btnFB.frame.size.width, height: btnFB.frame.size.height)
        if LOGIN_CONSTANT.objProfile.arrSocialLink.count == 0
        {
            btnFB.isHidden = true
            btnInstagram.isHidden = true
            btnTwitter.isHidden = true
            btnYouTube.isHidden = true
        }
            
        else if (LOGIN_CONSTANT.objProfile.arrSocialLink.count == 1)
        {
            let val:Int = Int(LOGIN_CONSTANT.objProfile.arrSocialLink[0] as! String)!
            switch val{
            case 1:
                btnFB.isHidden = false
                btnInstagram.isHidden = true
                btnTwitter.isHidden = true
                btnYouTube.isHidden = true
            case 2:
                btnFB.isHidden = true
                btnInstagram.isHidden = true
                btnTwitter.isHidden = false
                btnTwitter.frame = CGRect(x: btnFB.frame.origin.x, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                btnYouTube.isHidden = true
            case 3:
                btnFB.isHidden = true
                btnInstagram.isHidden = true
                btnTwitter.isHidden = true
                btnYouTube.isHidden = false
                btnYouTube.frame = CGRect(x: btnFB.frame.origin.x, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
            case 4:
                btnFB.isHidden = true
                btnInstagram.isHidden = false
                btnTwitter.isHidden = true
                btnYouTube.isHidden = true
                btnInstagram.frame = CGRect(x: btnFB.frame.origin.x, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                
            default:
                btnFB.isHidden = true
                btnInstagram.isHidden = true
                btnTwitter.isHidden = true
                btnYouTube.isHidden = true
            }
        }
        else if(LOGIN_CONSTANT.objProfile.arrSocialLink.count == 2)
        {
            btnFB.isHidden = true
            btnInstagram.isHidden = true
            btnTwitter.isHidden = true
            btnYouTube.isHidden = true
            if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("1")
            {
                btnFB.isHidden = false
                btnFB.frame = CGRect(x: self.view.frame.size.width/2 - (btnFB.frame.size.width + 5), y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("2")
                {
                    btnTwitter.isHidden = false
                    btnTwitter.frame = CGRect(x: self.view.frame.size.width/2 + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                }
                else if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("3")
                {
                    btnYouTube.isHidden = false
                    btnYouTube.frame = CGRect(x: self.view.frame.size.width/2 + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                }
                else if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("4")
                {
                    btnInstagram.isHidden = false
                    btnInstagram.frame = CGRect(x: self.view.frame.size.width/2 + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                }
                else{
                    
                }
            }
            else if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("2")
            {
                btnTwitter.isHidden = false
                btnTwitter.frame = CGRect(x: self.view.frame.size.width/2 - (btnFB.frame.size.height + 5), y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("3")
                {
                    btnYouTube.isHidden = false
                    btnYouTube.frame = CGRect(x: self.view.frame.size.width/2 + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                }
                else if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("4")
                {
                    btnInstagram.isHidden = false
                    btnInstagram.frame = CGRect(x: self.view.frame.size.width/2 + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                }
                else{
                    
                }
            }
            else if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("3")
            {
                btnYouTube.isHidden = false
                btnYouTube.frame = CGRect(x: self.view.frame.size.width/2 - (btnFB.frame.size.height + 5), y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("4")
                {
                    btnInstagram.isHidden = false
                    btnInstagram.frame = CGRect(x: self.view.frame.size.width/2 + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                }
                else{
                    
                }
            }
        }
            
        else if(LOGIN_CONSTANT.objProfile.arrSocialLink.count == 3)
        {
            btnFB.isHidden = true
            btnInstagram.isHidden = true
            btnTwitter.isHidden = true
            btnYouTube.isHidden = true
            if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("1")
            {
                btnFB.isHidden = false
                
                if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("2")
                {
                    btnTwitter.isHidden = false
                    btnTwitter.frame = CGRect(x: btnFB.frame.origin.x - (btnFB.frame.size.width + 5), y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                    if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("3")
                    {
                        btnYouTube.isHidden = false
                        btnYouTube.frame = CGRect(x: btnFB.frame.maxX + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                    }
                    else if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("4")
                    {
                        btnInstagram.isHidden = false
                        btnInstagram.frame = CGRect(x: btnFB.frame.maxX + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                    }
                    else{
                        
                    }
                }
                else if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("3")
                {
                    btnYouTube.isHidden = false
                    btnYouTube.frame = CGRect(x: btnFB.frame.origin.x - (btnFB.frame.size.width + 5), y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                    if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("4")
                    {
                        btnInstagram.isHidden = false
                        btnInstagram.frame = CGRect(x: btnFB.frame.maxX + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                    }
                    else{
                        
                    }
                }
                else{
                    
                }
            }
            else if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("2")
            {
                btnTwitter.isHidden = false
                btnTwitter.frame = CGRect(x: btnFB.frame.origin.x, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("3")
                {
                    btnYouTube.isHidden = false
                    btnYouTube.frame = CGRect(x: btnFB.frame.origin.x - (btnFB.frame.size.width + 5), y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                    if LOGIN_CONSTANT.objProfile.arrSocialLink.contains("4")
                    {
                        btnInstagram.isHidden = false
                        btnInstagram.frame = CGRect(x: btnFB.frame.maxX + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
                    }
                    else{
                        
                    }
                }
                else{
                    
                }
            }
        }
        else if(LOGIN_CONSTANT.objProfile.arrSocialLink.count == 4)
        {
            btnFB.isHidden = false
            btnInstagram.isHidden = false
            btnTwitter.isHidden = false
            btnYouTube.isHidden = false
            btnFB.frame = CGRect(x: self.view.frame.size.width/2 - (btnFB.frame.size.width * 2 + 10), y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
            btnTwitter.frame = CGRect(x: btnFB.frame.maxX + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
            btnYouTube.frame = CGRect(x: btnTwitter.frame.maxX + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
            btnInstagram.frame = CGRect(x: btnYouTube.frame.maxX + 5, y: btnFB.frame.origin.y, width: btnFB.frame.size.width, height: btnFB.frame.size.height)
        }
        else{
            
        }
        
        
        line2.isHidden = true
        lblLine3.isHidden = false
        lblLine4.isHidden = true
        btnTrips.titleLabel?.textColor = .darkGray
        btnTrip1.titleLabel?.textColor = .darkGray
        line1.isHidden = false
        btnAdd.isHidden = true
        txtaboutus.text = LOGIN_CONSTANT.objProfile.about
        //   let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: txtAbtMe.frame.width, font: txtAbtMe.font!, string: objProfile.about) + 25
        lblBuddiesNo.layer.cornerRadius = lblBuddiesNo.frame.size.height/2
        lblBuddiesNo.clipsToBounds = true
        lblNotifications.layer.cornerRadius = lblNotifications.frame.size.width/2
        lblNotifications.clipsToBounds = true
    }
    
    // MARK: - 
    // MARK: -  UIButton Action
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        UserDefaults.standard.set(false, forKey: "isProfileOpen")
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
        self.navigationController?.pushViewController(notificationVC, animated: true)
        
    }
    
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        UserDefaults.standard.set(false, forKey: "isProfileOpen")
        tblViewAccount.isHidden = true
        scrollViewMain.isHidden = false
        btnBack.isHidden = true
        UserDefaults.standard.set(true, forKey: "isProfileOpen")
    }
    @IBAction func onClickBtnCertificate(_ sender: UIButton) {
        UserDefaults.standard.set(false, forKey: "isProfileOpen")
        let value: CertificateBO = LOGIN_CONSTANT.objProfile.arrCertificate[0] as! CertificateBO
        var value1 = CertificateBO()
        if LOGIN_CONSTANT.objProfile.arrCertificate.count > 1 {
             value1 = LOGIN_CONSTANT.objProfile.arrCertificate[1] as! CertificateBO
        }
        
        switch sender.tag  {
        case 1:
            let certificationDetailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CertificationDetailsViewController") as? CertificationDetailsViewController
            certificationDetailsVC?.strSelectedItem = 1
            certificationDetailsVC?.fromVC = 3
             if value.Cert_type == "Scuba_Diving"
             {
                certificationDetailsVC?.objCert = value

             }
             else if value1.Cert_type == "Scuba_Diving"
             {
                certificationDetailsVC?.objCert = value1

             }
            else
             {
                
            }
            self.navigationController?.pushViewController(certificationDetailsVC!, animated: true)

        case 2:
            
            let freeDiverVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "FreeDiverCertificationDetailsViewController") as! FreeDiverCertificationDetailsViewController
            freeDiverVC.fromVC = 3
            if value.Cert_type == "Free_Diving"
            {
                freeDiverVC.objCert = value
            }
            else if value1.Cert_type == "Free_Diving"
            {
                freeDiverVC.objCert = value1
            }
            else
            {
                
            }
            self.navigationController?.pushViewController(freeDiverVC, animated: true)
        default:
            break
        }
        
    }
    @IBAction func onClickBtnSocialMedia(_ sender: UIButton) {
        var url = String()
        
        switch sender.tag {
        case 1:
            url = LOGIN_CONSTANT.objProfile.fb_url
        case 2:
            url = LOGIN_CONSTANT.objProfile.twtr_url

        case 3:
            url = LOGIN_CONSTANT.objProfile.ggle_url

        case 4:
            url = LOGIN_CONSTANT.objProfile.ingm_url

        default:
            break
        }
        
        if url.lowercased().hasPrefix("http://")==false{
            url = "http://" + (url)
        }
        let svc = SFSafariViewController(url: NSURL(string: url)! as URL)
        self.present(svc, animated: true, completion: nil)

        
    }
    
    @IBAction func onClickBtnBuddyList(_ sender: UIButton) {
        UserDefaults.standard.set(false, forKey: "isProfileOpen")
        self.callBuddyList()
    }
    @IBAction func onClickBtnAdd(_ sender: UIButton) {
        UserDefaults.standard.set(false, forKey: "isProfileOpen")
        let createTripVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CreateTripViewController") as! CreateTripViewController
        createTripVC.indexval = 2
        self.navigationController?.pushViewController(createTripVC, animated: true)
    }
    
    @IBAction func onClickBtnSelectionHeader(_ sender: UIButton) {
        if  sender.tag == 1 {
            line2.isHidden = true
            line1.isHidden = false
            btnTrips.titleLabel?.textColor = .darkGray
            lblLine3.isHidden = false
            lblLine4.isHidden = true
            btnAdd.isHidden = true
            btnTrip1.titleLabel?.textColor = .darkGray
        }
        else{
            line1.isHidden = true
            line2.isHidden = false
            btnAbout.titleLabel?.textColor = .darkGray
            lblLine3.isHidden = true
            lblLine4.isHidden = false
            btnAbtUs1.titleLabel?.textColor = .darkGray
            btnAdd.isHidden = false
            self.view.bringSubview(toFront: btnAdd)
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
    
    // MARK: -
    // MARK: - UitableViewDelegates & Datasources
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if tableView == tblTrip {
            self.prototypeCell = tblTrip.dequeueReusableCell(withIdentifier: "myTrips_Cell")! as! MyTripsTableViewCell
            let objVal:TripsDetailsBO = LOGIN_CONSTANT.objProfile.arrTrips[indexPath.row] as! TripsDetailsBO 
            let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: prototypeCell.lblDesc.frame.size.width, font: prototypeCell.lblDesc.font, string: objVal.trip_desc)
            objVal.cellHgt = cellHgt + 25
            return 237 + cellHgt
        }
        else{
            return 45
        }
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if tableView == tblTrip
        {
            return LOGIN_CONSTANT.objProfile.arrTrips.count
        }
        else{
            return arrNames.count
        }
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        if tableView == tblTrip
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "myTrips_Cell", for: indexPath) as! MyTripsTableViewCell
            let objTrip: TripsDetailsBO = LOGIN_CONSTANT.objProfile.arrTrips[indexPath.row] as! TripsDetailsBO 
            cell.imgBg.layer.borderWidth = 0.5
            cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
            cell.imgBg.layer.cornerRadius = 8.0
            cell.imgBg.layer.shadowColor = UIColor.lightGray.cgColor
            
            cell.lblNo.layer.cornerRadius = cell.lblNo.frame.size.width/2
            cell.lblNo.clipsToBounds = true
            cell.lblNo.text = objTrip.no_of_participant
            
            cell.lblName.text = objTrip.activity
            cell.lblDate.text = objTrip.event_date
            cell.lblDesc.text = objTrip.trip_desc
            cell.lblAddress.text = objTrip.event_loc
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
                let editVC = self.storyboard!.instantiateViewController(withIdentifier: "CreateTripViewController") as! CreateTripViewController
                editVC.objTrips = objTrip
                editVC.isFromUpdate = true
                editVC.onClickDelete = {() -> Void in
                    LOGIN_CONSTANT.objProfile.arrTrips.remove(objTrip)
                    self.tblTrip.reloadData()
                }
                editVC.onClickEdit = {(objValue) -> Void in
                    LOGIN_CONSTANT.objProfile.arrTrips.remove(objTrip)
                    LOGIN_CONSTANT.objProfile.arrTrips.add(objValue)
                    self.tblTrip.reloadData()
                }
                self.navigationController?.pushViewController(editVC, animated: true)
            }
            return cell
        }
        else{
            let cell = tableView.dequeueReusableCell(withIdentifier: "myAccount_Cell", for: indexPath) as! MyAccountTableViewCell
            cell.lblNames.text = arrNames[indexPath.row];
            cell.iconAccount.image = UIImage.init(named: arrImages[indexPath.row])
            cell.lblLine.isHidden = arrNames.count - 1 == indexPath.row ? true : false
            return cell
        }
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if tableView == tblTrip
        {
            
        }
        else{
        switch indexPath.row {
        case 0:
            UserDefaults.standard.set(false, forKey: "isProfileOpen")
            tblViewAccount.isHidden = true
            scrollViewMain.isHidden = false
            btnBack.isHidden = true
            UserDefaults.standard.set(true, forKey: "isProfileOpen")
            break
        case 1:
            let editProfileVC:UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "EditProfileViewController") as UIViewController
            self.navigationController?.pushViewController(editProfileVC, animated: true)
            break
        case 2:
            let certificateVC:UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "CertificateListViewController") as UIViewController
            self.navigationController?.pushViewController(certificateVC, animated: true)
            break
        case 3:
            UserParser.callProfileDetailsService("", complete: { (status, strMsg, objProfile) in
                if status
                {
                    DispatchQueue.main.async {
                        let tripsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "MyTripsViewController") as! MyTripsViewController
                        self.navigationController?.pushViewController(tripsVC, animated: true)
                    }
                }
                else{
                    DispatchQueue.main.async {
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                }
            })            
            break
        case 4:
            self.callBuddyList()
            
        case 5:
            let listingVC:UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "MyListingViewController") as UIViewController
            self.navigationController?.pushViewController(listingVC, animated: true)
            break
            
        case 6:
            let reviewVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "MyReviewsViewController") as! MyReviewsViewController
            self.navigationController?.pushViewController(reviewVC, animated: true)
            break

        case 7:
            let chngPassVC:UIViewController = self.storyboard!.instantiateViewController(withIdentifier: "ChangePassViewController") as UIViewController
            self.navigationController?.pushViewController(chngPassVC, animated: true)
            break
        case 8:
        
            let alert = UIAlertController(title: "BLUE BUDDY",
                                          message: "Do you want to logout?",
                                          preferredStyle: UIAlertControllerStyle.alert)
            let settingAction = UIAlertAction(title: "Yes", style: .default, handler: {(_ action: UIAlertAction) -> Void in
                
                let loginVC = APP_DELEGATE.stryBoard?.instantiateViewController(withIdentifier: "LoginViewController")
                APP_DELEGATE.navController =  UINavigationController(rootViewController: loginVC!)
                APP_DELEGATE.navController?.navigationBar.isHidden = true
                APP_DELEGATE.window?.rootViewController = APP_DELEGATE.navController
                UserDefaults.standard.set(false, forKey: kisLogin)
                UserDefaults.standard.set(false, forKey: kisFBLogin)
                UserDefaults.standard.set("", forKey: kFBID)
                UserDefaults.standard.set("", forKey: kUserPass)
                UserDefaults.standard.set("", forKey: kUserEmail)
                UserDefaults.standard.set(false, forKey: "isProfileOpen")
                self.navigationController?.popToRootViewController(animated: true)
            })
            let cancelAction = UIAlertAction(title: "No", style: .cancel, handler: nil)
            
            alert.addAction(cancelAction)
            alert.addAction(settingAction)
            
            self.present(alert, animated: true, completion: nil)
        
        default:
            break
        }
        }
    }

    // MARK: - 
    // MARK: - ScrollView Delegates
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollViewMain.contentOffset.y >= viewHeader.frame.size.height
        {
            selectionList1.isHidden = true
            self.view.bringSubview(toFront: selectionList1)
            lblTitle.text = LOGIN_CONSTANT.objProfile.first_name.capitalized + " " + LOGIN_CONSTANT.objProfile.last_name.capitalized
            scrollViewMain.isScrollEnabled = false
            scrollViewMain.setContentOffset(CGPoint(x: 0,y: viewHeader.frame.size.height), animated: true)
            scrollViewMain.isScrollEnabled = true
            self.tblTrip.isScrollEnabled = true
        }
        else{
            self.tblTrip.isScrollEnabled  = false
            selectionList1.isHidden = true
            lblTitle.text = "PROFILE"
        }
    }
    
    func kilScroll() {
        isScrollKilled = true
        scrollViewMain.isScrollEnabled = false
        scrollViewMain.isScrollEnabled = true
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView){
        // Test the offset and calculate the current page after scrolling ends
        
        if scrollView == scrollViewBtm
        {
            let pageWidth:CGFloat = scrollView.frame.width
            let currentPage:CGFloat = floor((scrollView.contentOffset.x-pageWidth/2)/pageWidth)+1
            if  Int(currentPage) == 0 {
                line2.isHidden = true
                line1.isHidden = false
                lblLine3.isHidden = false
                lblLine4.isHidden = true
                btnTrips.titleLabel?.textColor = .darkGray
                btnTrip1.titleLabel?.textColor = .darkGray
                btnAdd.isHidden = true
            }
            else{
                line1.isHidden = true
                line2.isHidden = false
                btnAbout.titleLabel?.textColor = .darkGray
                lblLine3.isHidden = true
                lblLine4.isHidden = false
                btnAbtUs1.titleLabel?.textColor = .darkGray
                btnAdd.isHidden = false
            }
        }
    }

    // MARK: -
    // MARK: - SafariViewController Delegates
    func safariViewControllerDidFinish(_ controller: SFSafariViewController)
    {
        controller.dismiss(animated: true, completion: nil)
    }
    
    func callBuddyList()
    {
        DispatchQueue.main.async {
            let buddyVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "BuddyListViewController") as! BuddyListViewController
            self.navigationController?.pushViewController(buddyVC, animated: true)
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

//extension TRIPSViewController: UIScrollViewDelegate {
//    func scrollViewDidScroll(_ scrollView: UIScrollView) {
//        // determining whether scrollview is scrolling up or down
//        goingUp = scrollView.panGestureRecognizer.translation(in: scrollView).y < 0
//        
//        // maximum contentOffset y that parent scrollView can have
//        let parentViewMaxContentYOffset = scrollViewMain.contentSize.height - scrollViewMain.frame.height
//        
//        // if scrollView is going upwards
//        if goingUp! {
//            // if scrollView is a child scrollView
//            
//            if scrollView == childScrollView {
//                // if parent scroll view is't scrolled maximum (i.e. menu isn't sticked on top yet)
//                if scrollViewMain.contentOffset.y < parentViewMaxContentYOffset && !childScrollingDownDueToParent {
//                    
//                    // change parent scrollView contentOffset y which is equal to minimum between maximum y offset that parent scrollView can have and sum of parentScrollView's content's y offset and child's y content offset. Because, we don't want parent scrollView go above sticked menu.
//                    // Scroll parent scrollview upwards as much as child scrollView is scrolled
//                    // Sometimes parent scrollView goes in the middle of screen and stucks there so max is used.
//                    scrollViewMain.contentOffset.y = max(min(scrollViewMain.contentOffset.y + childScrollView.contentOffset.y, parentViewMaxContentYOffset), 0)
//                    
//                    // change child scrollView's content's y offset to 0 because we are scrolling parent scrollView instead with same content offset change.
//                    childScrollView.contentOffset.y = 0
//                }
//            }
//        }
//            // Scrollview is going downwards
//        else {
//            
//            if scrollView == childScrollView {
//                // when child view scrolls down. if childScrollView is scrolled to y offset 0 (child scrollView is completely scrolled down) then scroll parent scrollview instead
//                // if childScrollView's content's y offset is less than 0 and parent's content's y offset is greater than 0
//                if childScrollView.contentOffset.y < 0 && scrollViewMain.contentOffset.y > 0 {
//                    
//                    // set parent scrollView's content's y offset to be the maximum between 0 and difference of parentScrollView's content's y offset and absolute value of childScrollView's content's y offset
//                    // we don't want parent to scroll more that 0 i.e. more downwards so we use max of 0.
//                    scrollViewMain.contentOffset.y = max(scrollViewMain.contentOffset.y - abs(childScrollView.contentOffset.y), 0)
//                }
//            }
//            
//            // if downward scrolling view is parent scrollView
//            if scrollView == scrollViewMain {
//                // if child scrollView's content's y offset is greater than 0. i.e. child is scrolled up and content is hiding up
//                // and parent scrollView's content's y offset is less than parentView's maximum y offset
//                // i.e. if child view's content is hiding up and parent scrollView is scrolled down than we need to scroll content of childScrollView first
//                if childScrollView.contentOffset.y > 0 && scrollViewMain.contentOffset.y < parentViewMaxContentYOffset {
//                    // set if scrolling is due to parent scrolled
//                    childScrollingDownDueToParent = true
//                    // assign the scrolled offset of parent to child not exceding the offset 0 for child scroll view
//                    childScrollView.contentOffset.y = max(childScrollView.contentOffset.y - (parentViewMaxContentYOffset - scrollViewMain.contentOffset.y), 0)
//                    // stick parent view to top coz it's scrolled offset is assigned to child
//                    scrollViewMain.contentOffset.y = parentViewMaxContentYOffset
//                    childScrollingDownDueToParent = false
//                }
//            }
//        }
//}
//}
