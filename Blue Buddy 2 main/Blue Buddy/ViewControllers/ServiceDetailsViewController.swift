//
//  ServiceDetailsViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/26/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import FloatRatingView
import Kingfisher

class ServiceDetailsViewController: UIViewController, FloatRatingViewDelegate, UITableViewDataSource, UITableViewDelegate, UITextViewDelegate, UIGestureRecognizerDelegate{

    @IBOutlet weak var imgFeatured: UIImageView!
    @IBOutlet weak var btnHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnSubmit: UIButton!
    @IBOutlet weak var contentVhgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewRateTopConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewReviewHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var tblViewHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var tblViewReviews: UITableView!
    @IBOutlet weak var lblNoData: UILabel!
    @IBOutlet weak var btnReviews: UIButton!
    @IBOutlet weak var viewRateHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var txtViewHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewRateInnerHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var txtDesc: UITextView!
    @IBOutlet weak var txtTitle: UITextField!
    @IBOutlet weak var rating: FloatRatingView!
    @IBOutlet weak var btnRate: UIButton!
    @IBOutlet weak var viewInnerRate: UIView!
    @IBOutlet weak var lblDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblDesc: UILabel!
    @IBOutlet weak var lblEmail: UILabel!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var lblPostedBy: UILabel!
    @IBOutlet weak var lblServiceName: UILabel!
    @IBOutlet weak var rateService: FloatRatingView!
    @IBOutlet weak var imgService: UIImageView!
    @IBOutlet weak var viewReview: UIView!
    @IBOutlet weak var viewRate: UIView!
    @IBOutlet weak var viewDesc: UIView!
    @IBOutlet weak var btnMessage: UIButton!
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var lblBadgeCount: UILabel!
    @IBOutlet weak var constraintHghtContactDetails: NSLayoutConstraint!
    
    @IBOutlet weak var vwContact: UIView!
    @IBOutlet weak var constraintHghtVwContact: NSLayoutConstraint!
    @IBOutlet weak var vwContactEmail: UIView!
    @IBOutlet weak var vwContactPhone: UIView!
    var objServiceDetails = ServiceDetailsBO()
    var tblHgt = CGFloat()
    var value = CGFloat()
    var indexVal = Int()
    var prototypeCell = ReviewTableViewCell()
    override func viewDidLoad() {
        super.viewDidLoad()

        
        if objServiceDetails.is_hidden {
                hideContactDetails()
        }

        
        let myColor = UIColor.lightGray
        txtDesc.layer.cornerRadius = 5.0
        txtDesc.layer.borderWidth = 1.0
        txtDesc.layer.borderColor = myColor.cgColor
        txtTitle.layer.cornerRadius = 5.0
        txtTitle.layer.borderWidth = 1.0
        txtTitle.layer.borderColor = myColor.cgColor
        viewRateHgtConstraint.constant = 55
        viewReviewHgtConstraint.constant = 55
        viewDesc.layer.cornerRadius = 5.0
        viewDesc.layer.borderWidth = 1.0
        viewDesc.layer.borderColor = myColor.cgColor
        vwContact.layer.cornerRadius = 5.0
        vwContact.layer.borderWidth = 1.0
        vwContact.layer.borderColor = myColor.cgColor
        viewReview.layer.cornerRadius = 5.0
        viewReview.layer.borderWidth = 1.0
        viewReview.layer.borderColor = myColor.cgColor
        viewRate.layer.cornerRadius = 5.0
        viewRate.layer.borderWidth = 1.0
        viewRate.layer.borderColor = myColor.cgColor
        viewInnerRate.isHidden = true
        tblViewReviews.isHidden = true
        rating.delegate = self
        
        btnRate.layer.cornerRadius = 5.0
        btnReviews.layer.cornerRadius = 5.0
        
        let boldAttributes = [NSForegroundColorAttributeName: UIColor.black, NSFontAttributeName: lblEmail.font!] as [String : Any]
        let yourOtherAttributes = [NSForegroundColorAttributeName: UIColor.darkGray, NSFontAttributeName: lblEmail.font!] as [String : Any]
        
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: lblDesc.frame.width, font: lblDesc.font, string: objServiceDetails.service_desc) + 25
        lblServiceName.text = "Service Offered: " + objServiceDetails.service_type
        lblPostedBy.text = objServiceDetails.user_name
        
        let titleImgString2 = NSMutableAttributedString(string: "Email : ", attributes: boldAttributes)
        titleImgString2.append(NSAttributedString(string: objServiceDetails.user_email, attributes: yourOtherAttributes))
        lblEmail.attributedText = titleImgString2
        
        let titleImgString3 = NSMutableAttributedString(string: "Phone No : ", attributes: boldAttributes)
        titleImgString3.append(NSAttributedString(string: objServiceDetails.user_Phone, attributes: yourOtherAttributes))
        lblPhone.attributedText = titleImgString3
        
//        lblEmail.text = objServiceDetails.user_email
//        lblPhone.text = objServiceDetails.user_Phone
        let constraintRect = CGSize(width: self.view.frame.size.width, height: lblPhone.frame.height)
        //        let constraintRect = CGSize(width: width, height: .greatestFiniteMagnitude)//DBL_MAX
        let boundingBox = objServiceDetails.user_Phone.boundingRect(with: constraintRect, options: [.usesLineFragmentOrigin, .usesFontLeading], attributes: [NSFontAttributeName: lblPhone.font], context: nil)
        
        lblPhone.frame = CGRect(x: lblPhone.frame.origin.x, y: lblPhone.frame.origin.y, width: boundingBox.width, height: lblPhone.frame.size.height)
        lblPhone.translatesAutoresizingMaskIntoConstraints = true
        lblDesc.text = objServiceDetails.service_desc
        lblDescHgtConstraint.constant = valHgt
        viewDescHgtConstraint.constant = 65 + valHgt
        rateService.rating = Float(objServiceDetails.rating)!
        
        contentVhgtConstraint.constant = viewDesc.frame.maxY + 170 + valHgt + constraintHghtVwContact.constant
        imgService.kf.setImage(with: URL(string:objServiceDetails.service_image), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.handleTap(sender:)))
        tap.delegate = self as UIGestureRecognizerDelegate
        viewHeader.addGestureRecognizer(tap)
        
        imgFeatured.isHidden = objServiceDetails.is_featured ? false : true
        value = contentVhgtConstraint.constant
        
        if objServiceDetails.user_id == LOGIN_CONSTANT.objProfile.userId
        {
            btnHgtConstraint.constant = 0
        }
        if objServiceDetails.is_reviewed
        {
            viewRateHgtConstraint.constant = 0
            viewRate.isHidden = true
            viewRateTopConstraint.constant = 0
        }
        lblNoData.isHidden = true
        for index in 0..<objServiceDetails.arrReviews.count
        {
            let objVal = objServiceDetails.arrReviews[index] as! ReviewBO
            let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: lblDesc.frame.size.width, font: lblDesc.font, string: objVal.reviewDesc)
            
            tblHgt += 198 + cellHgt - 25
        }
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

    func handleTap(sender: UITapGestureRecognizer? = nil) {
        // handling code
        Common.displayImageLarger(viewController: self, displayImageUrl:URL(string:objServiceDetails.service_image)!)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // MARK: -
    // MARK: - UIButton Action
    @IBAction func onClickBtnMsg(_ sender: UIButton) {
        ProfileViewController.callChatDetails(objServiceDetails.firebase_id, self)
    }
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        
        if indexVal == 1 || indexVal == 2
        {
            var isFound:Bool = false
            if let viewControllers = navigationController?.viewControllers {
                for viewController in viewControllers {
                    // some process
                    if viewController.isKind(of: indexVal == 1 ? ServicesViewController.self : MyListingViewController.self) {
                        isFound = true
                        if indexVal == 1 {
                            NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateServiceList"), object: nil)
                        }
                        else if indexVal == 2
                        {
                            NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateMyList"), object: nil)
                        }
                        else{
                            
                        }
                        self.navigationController?.popToViewController(viewController, animated: true)
                    }
                }
                if !isFound
                {
                    self.navigationController?.popViewController(animated: true)
                }
            }
            
        }
        else
        {
            self.navigationController?.popViewController(animated: true)
        }

    }

    @IBAction func onClickBtnRate(_ sender: UIButton) {
        
        sender.isSelected = !sender.isSelected
        if sender.isSelected
        {
            viewRateInnerHgtConstraint.constant = btnSubmit.frame.maxY + 10
            viewRateHgtConstraint.constant = viewRateInnerHgtConstraint.constant + 55
            viewInnerRate.isHidden = false
            contentVhgtConstraint.constant += viewRateInnerHgtConstraint.constant
            value = contentVhgtConstraint.constant
            
            
        }
        else
        {
            viewRateHgtConstraint.constant = 55
            viewRateInnerHgtConstraint.constant = 0
            viewInnerRate.isHidden = true
            contentVhgtConstraint.constant -= btnSubmit.frame.maxY + 10
            value = contentVhgtConstraint.constant
            
        }

    }
    @IBAction func onClickBtnSubmit(_ sender: UIButton) {
        
        self.view.endEditing(true)
        if rating.rating == 0
        {
            Common.showAlert(message: "Please give rating", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtTitle.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your review title", title: "BLUE BUDDY", viewController: self)
        }
            
        else if (txtDesc.text == "Description") || (txtDesc.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your review description", title: "BLUE BUDDY", viewController: self)
        }
        else
        {
            let dictVal = NSMutableDictionary()
            dictVal["rev_title"] = txtTitle.text
            dictVal["rev_description"] = txtDesc.text
            dictVal["rev_rating"] = String(rating.rating)
            dictVal["rev_for"] = "service"
            dictVal["for_id"] = objServiceDetails.service_id
            MarketParser.callCreateReviewService(dictVal, complete: { (status, strMsg, objValue) in
                if status
                {
                    DispatchQueue.main.async {
                        self.objServiceDetails.arrReviews.add(objValue)
                        let val = self.tblHgt
                        self.tblHgt = 0
                        for index in 0..<self.objServiceDetails.arrReviews.count
                        {
                            let objVal = self.objServiceDetails.arrReviews[index] as! ReviewBO
                            let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: self.lblDesc.frame.size.width, font: self.lblDesc.font, string: objVal.reviewDesc)
                            
                            self.tblHgt += 198 + cellHgt - 25
                        }
                        self.tblViewReviews.reloadData()
                        self.viewRateHgtConstraint.constant = 0
                        self.viewRateInnerHgtConstraint.constant = 0
                        self.viewRateTopConstraint.constant = 0
                        self.viewInnerRate.isHidden = true
                        self.viewRate.isHidden = true
                        self.contentVhgtConstraint.constant -= self.btnSubmit.frame.maxY + 10
//                        let hgt = CGFloat(self.objServiceDetails.arrReviews.count * 200)
                        self.tblViewHgtConstraint.constant = CGFloat(self.tblHgt)
                        self.viewReviewHgtConstraint.constant = CGFloat(55 + self.tblHgt)
                        self.tblViewReviews.isHidden = false
                        self.lblNoData.isHidden = true
                        self.contentVhgtConstraint.constant += self.tblHgt - val
                        self.value = self.contentVhgtConstraint.constant
                        
                    }
                }
            })
        }

    }
    @IBAction func onClickBtnRevealReview(_ sender: UIButton) {
        
        sender.isSelected = !sender.isSelected
        let hgt: CGFloat = objServiceDetails.arrReviews.count != 0 ? tblHgt : 45
        if sender.isSelected
        {
            tblViewHgtConstraint.constant = CGFloat(hgt)
            viewReviewHgtConstraint.constant = CGFloat(55 + hgt)
            tblViewReviews.isHidden = objServiceDetails.arrReviews.count != 0 ? false : true
            lblNoData.isHidden = objServiceDetails.arrReviews.count != 0 ? true : false
            contentVhgtConstraint.constant += hgt
            value = contentVhgtConstraint.constant
            
        }
        else
        {
            viewReviewHgtConstraint.constant = 55
            tblViewHgtConstraint.constant = 0
            tblViewReviews.isHidden = true
            lblNoData.isHidden = true
            contentVhgtConstraint.constant -= hgt
            value = contentVhgtConstraint.constant
            
        }
    }
    
    // MARK: -
    // MARK: - FloatRatingViewDelegate
    func floatRatingView(_ ratingView: FloatRatingView, didUpdate rating: Float)
    {
        
    }
    
    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return objServiceDetails.arrReviews.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "review_Cell", for: indexPath) as! ReviewTableViewCell
        let objVal = objServiceDetails.arrReviews[indexPath.row] as! ReviewBO
        cell.lblDate.text = objVal.date
        cell.lblName.text = objVal.user_name
        cell.lblTitle.text = objVal.review_title
        cell.lblreview.text = objVal.reviewDesc
        cell.lblDescHgtConstraint.constant = objVal.cellHgt
        cell.viewRating.rating = Float(objVal.rating)!
        cell.imgUser.kf.setImage(with: URL(string:objVal.user_pic), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        cell.imgUser.layer.cornerRadius = cell.imgUser.frame.size.height/2
        cell.imgUser.clipsToBounds = true
        
        if objVal.user_id == LOGIN_CONSTANT.objProfile.userId
        {
            cell.btnProfile.setImage(#imageLiteral(resourceName: "edit1.png"), for: .normal)
            cell.imgUser.isHidden = true
        }
        else{
            cell.btnProfile.setImage(nil, for: .normal)
            cell.imgUser.isHidden = false
        }
        cell.onClickProfile = {() -> Void in
            if objVal.user_id == LOGIN_CONSTANT.objProfile.userId
            {
                DispatchQueue.main.async {
                    let reviewVC:EditReviewViewController = self.storyboard!.instantiateViewController(withIdentifier: "EditReviewViewController") as! EditReviewViewController
                    let objReview = objVal
                    objReview.review_for = "service"
                    objReview.for_id = self.objServiceDetails.service_id
                    reviewVC.objReview = objReview
                    reviewVC.onClickUpdate = {(objRevVal) -> Void in
                        self.objServiceDetails.arrReviews.remove(objVal)
                        self.objServiceDetails.arrReviews.add(objRevVal)
                        self.tblViewReviews.reloadData()
                    }
                    self.navigationController?.pushViewController(reviewVC, animated: true)
                }
            }
            else
            {

                UserParser.callProfileDetailsService(objVal.user_id) { (status, strMsg, objProfile) in
                    if status
                    {
                        DispatchQueue.main.async {
                            let profileVC:ProfileViewController = self.storyboard!.instantiateViewController(withIdentifier: "ProfileViewController") as! ProfileViewController
                            profileVC.objProfile = objProfile
                            self.navigationController?.pushViewController(profileVC, animated: true)
                        }
                    }
                    else{
                        DispatchQueue.main.async {
                            Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                        }
                    }
                }
            }
            
        }

        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        self.prototypeCell = tblViewReviews.dequeueReusableCell(withIdentifier: "review_Cell")! as! ReviewTableViewCell
        
        let objVal = objServiceDetails.arrReviews[indexPath.row] as! ReviewBO
        let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: prototypeCell.lblreview.frame.size.width, font: prototypeCell.lblreview.font, string: objVal.reviewDesc)
        objVal.cellHgt = cellHgt + 25
        return 198 + cellHgt - 25
        //        return 198
    }
    
    // MARK: -
    // MARK: - UITextViewDelegates
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtDesc.text == "Description"
        {
            txtDesc.text = ""
            txtDesc.textColor = .black
        }
    }
    func textViewDidEndEditing(_ textView: UITextView) {
        if txtDesc.text.isEmpty
        {
            txtDesc.text = "Description"
            txtDesc.textColor = .lightGray
            
        }
    }
    
    func textViewDidChange(_ textView: UITextView) {
        
        self.textViewDynamicallyIncreaseSize()
        
    }
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        let length = (textView.text?.characters.count)! + text.characters.count - range.length
        if length < 150 {
            return true
        }
        else{
            return false
        }
    }
    
    // MARK: -
    // MARK: - Other Methods
    func textViewDynamicallyIncreaseSize() {
        let contentSize = self.txtDesc.sizeThatFits(self.txtDesc.bounds.size)
        let higntcons = contentSize.height < 55 ? 55 : contentSize.height
        txtViewHgtConstraint.constant = higntcons
        viewRateHgtConstraint.constant = (325 + higntcons) - 55
        contentVhgtConstraint.constant = value + higntcons
        viewRateInnerHgtConstraint.constant = (270 + higntcons) - 55
    }
    
    func hideContactDetails() {
        
//        if true
//        {
////            constraintHghtContactDetails.constant -= 20
//            vwContactPhone.isHidden = true
//            vwContactEmail.isHidden = true
//        }
//        else
//        {
//            //            constraintHghtContactDetails.constant = 200
//            //            contentVHgtConstraint.constant += constraintHghtContactDetails.constant
//            //            viewContact.isHidden = false
//            //            value = contentVHgtConstraint.constant
//        }
        
        
        if true
        {
            let intRemoveHght = constraintHghtVwContact.constant + 100
            contentVhgtConstraint.constant -= intRemoveHght
            constraintHghtVwContact.constant = 0
            vwContact.isHidden = true
            value = contentVhgtConstraint.constant
        }
        else
        {
            //            constraintHghtContactDetails.constant = 200
            //            contentVHgtConstraint.constant += constraintHghtContactDetails.constant
            //            viewContact.isHidden = false
            //            value = contentVHgtConstraint.constant
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
