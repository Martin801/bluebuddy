//
//  CourseDetailsViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/1/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import FloatRatingView

class CourseDetailsViewController: UIViewController, FloatRatingViewDelegate, UITableViewDelegate, UITableViewDataSource, UITextViewDelegate, UIGestureRecognizerDelegate {
    /**
     Returns the rating value when touch events end
     */
    @IBOutlet weak var imgFeatured: UIImageView!

    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var btnMsg: UIButton!
    @IBOutlet weak var lblNoData: UILabel!
    @IBOutlet weak var btnReview: UIButton!
    @IBOutlet weak var txtDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnSubmit: UIButton!
    @IBOutlet weak var contentVHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewRateTopConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblCourse: UILabel!

    @IBOutlet weak var btnMessageHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewRateHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewReviewHgtConstraints: NSLayoutConstraint!
    @IBOutlet weak var tblViewHgtConstraints: NSLayoutConstraint!
    @IBOutlet weak var viewInnerRateHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var tblViewReview: UITableView!
    @IBOutlet weak var txtViewDesc: UITextView!
    @IBOutlet weak var txtTitleReview: UITextField!
    @IBOutlet weak var rateView: FloatRatingView!
    @IBOutlet weak var viewInnerRate: UIView!
    @IBOutlet weak var btnRate: UIButton!
    @IBOutlet weak var viewDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblAddress: UILabel!
    @IBOutlet weak var lblPrice: UILabel!
    @IBOutlet weak var lblDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblDesc: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var courseRating: FloatRatingView!
    @IBOutlet weak var imgBG: UIImageView!
    @IBOutlet weak var viewReview: UIView!
    @IBOutlet weak var viewRate: UIView!
    @IBOutlet weak var viewDesc: UIView!
    @IBOutlet weak var contentV: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var lblBadgeCount: UILabel!
    var prototypeCell = ReviewTableViewCell()
    var value = CGFloat()
    var tblHgt = CGFloat()
    var indexVal = Int()
    var objDetails = CoursesDetailsBO()
    override func viewDidLoad() {
        super.viewDidLoad()

        
        let boldAttributes = [NSForegroundColorAttributeName: UIColor.black, NSFontAttributeName: lblAddress.font!] as [String : Any]
        let yourOtherAttributes = [NSForegroundColorAttributeName: UIColor.darkGray, NSFontAttributeName: lblDesc.font!] as [String : Any]
        
        let titleImgString = NSMutableAttributedString(string: "Agency Name : ", attributes: boldAttributes)
        titleImgString.append(NSAttributedString(string: objDetails.agency_name, attributes: yourOtherAttributes))
        lblName.attributedText = titleImgString
        
        let titleImgString1 = NSMutableAttributedString(string: "Course Duration : ", attributes: boldAttributes)
        titleImgString1.append(NSAttributedString(string: objDetails.duration, attributes: yourOtherAttributes))
        lblCourse.attributedText = titleImgString1
        
        let titleImgString2 = NSMutableAttributedString(string: "Price : ", attributes: boldAttributes)
        titleImgString2.append(NSAttributedString(string: "$" + objDetails.price, attributes: yourOtherAttributes))
        lblPrice.attributedText = titleImgString2
        
        let titleImgString4 = NSMutableAttributedString(string: "Location : ", attributes: boldAttributes)
        titleImgString4.append(NSAttributedString(string: objDetails.user_loc, attributes: yourOtherAttributes))
        lblAddress.attributedText = titleImgString4
        
        lblDesc.text = objDetails.course_desc
        let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: lblDesc.frame.width, font: lblDesc.font, string: objDetails.course_desc) + 25
        lblDescHgtConstraint.constant = valHgt < 45 ? 45 : valHgt
        viewDescHgtConstraint.constant += valHgt < 45 ? 45 : valHgt 
        courseRating.rating = Float(objDetails.rating)

        btnRate.layer.cornerRadius = 5.0
        btnReview.layer.cornerRadius = 5.0
        
        viewRateHgtConstraint.constant = 55
        viewReviewHgtConstraints.constant = 55
        
        let myColor = UIColor.lightGray
        txtViewDesc.layer.cornerRadius = 5.0
        txtViewDesc.layer.borderWidth = 1.0
        txtViewDesc.layer.borderColor = myColor.cgColor
        
        txtTitleReview.layer.cornerRadius = 5.0
        txtTitleReview.layer.borderWidth = 1.0
        txtTitleReview.layer.borderColor = myColor.cgColor
        
        viewDesc.layer.cornerRadius = 5.0
        viewDesc.layer.borderWidth = 1.0
        viewDesc.layer.borderColor = myColor.cgColor
        
        viewRate.layer.cornerRadius = 5.0
        viewRate.layer.borderWidth = 1.0
        viewRate.layer.borderColor = myColor.cgColor
        
        viewReview.layer.cornerRadius = 5.0
        viewReview.layer.borderWidth = 1.0
        viewReview.layer.borderColor = myColor.cgColor
        
        viewInnerRate.isHidden = true
        tblViewReview.isHidden = true
        
        rateView.delegate = self
        
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        
        
        
        let url = URL(string:objDetails.course_image)
        imgBG.kf.setImage(with: url, placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        imgFeatured.isHidden = objDetails.is_featured ? false : true
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.handleTap(sender:)))
        tap.delegate = self as UIGestureRecognizerDelegate
        viewHeader.addGestureRecognizer(tap)
        
        if objDetails.user_id == LOGIN_CONSTANT.objProfile.userId
        {
            btnMessageHgtConstraint.constant = 0
            btnMsg.isHidden = true
        }
        if objDetails.is_reviewed
        {
            viewRateHgtConstraint.constant = 0
            viewRate.isHidden = true
            viewRateTopConstraint.constant = 0
        }
       
        lblNoData.isHidden = true
        for index in 0..<objDetails.arrReviews.count
        {
            let objVal = objDetails.arrReviews[index] as! ReviewBO
            let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: lblName.frame.size.width, font: lblName.font, string: objVal.reviewDesc)
            
            tblHgt += 198 + cellHgt - 25
        }
        contentVHgtConstraint.constant = viewDesc.frame.maxY + 170 + valHgt
        value = contentVHgtConstraint.constant
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
        Common.displayImageLarger(viewController: self, displayImageUrl:URL(string:objDetails.course_image)!)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onClickBtnMsg(_ sender: UIButton) {
        ProfileViewController.callChatDetails(objDetails.firebase_id, self)
    }
    @IBAction func onClickBtnBack( _ sender: UIButton)
    {
        if indexVal == 1 || indexVal == 2
        {
            var isFound:Bool = false
            if let viewControllers = navigationController?.viewControllers {
                for viewController in viewControllers {
                    // some process
                    if viewController.isKind(of: indexVal == 1 ? CourseListViewController.self : MyListingViewController.self) {
                        isFound = true
                        if indexVal == 1 {
                            NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateCourseList"), object: nil)
                        }
                        else if  indexVal == 2
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
        else{
            self.navigationController?.popViewController(animated: true)
        }

    }
    @IBAction func onClickBtnRevealRateView(_ sender: UIButton) {
        sender.isSelected = !sender.isSelected
        if sender.isSelected
        {
            viewInnerRateHgtConstraint.constant = btnSubmit.frame.maxY + 10
            viewRateHgtConstraint.constant = viewInnerRateHgtConstraint.constant + 55
            viewInnerRate.isHidden = false
            contentVHgtConstraint.constant += viewInnerRateHgtConstraint.constant
            value = contentVHgtConstraint.constant
        }
        else
        {
            viewRateHgtConstraint.constant = 55
            viewInnerRateHgtConstraint.constant = 0
            viewInnerRate.isHidden = true
            contentVHgtConstraint.constant -= btnSubmit.frame.maxY + 10
            value = contentVHgtConstraint.constant
        }
    }
    
    @IBAction func onClickBtnSubmit(_ sender: UIButton) {
        
        self.view.endEditing(true)
        if rateView.rating == 0
        {
            Common.showAlert(message: "Please give rating", title: "BLUE BUDDY", viewController: self)
        }
        else if (txtTitleReview.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your review title", title: "BLUE BUDDY", viewController: self)
        }
            
        else if (txtViewDesc.text == "Description") || (txtViewDesc.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your review description", title: "BLUE BUDDY", viewController: self)
        }
        else
        {
            let dictVal = NSMutableDictionary()
            dictVal["rev_title"] = txtTitleReview.text
            dictVal["rev_description"] = txtViewDesc.text
            dictVal["rev_rating"] = String(rateView.rating)
            dictVal["rev_for"] = "course"
            dictVal["for_id"] = objDetails.course_id
            MarketParser.callCreateReviewService(dictVal, complete: { (status, strMsg, objValue) in
                if status
                {
                    DispatchQueue.main.async {
                        let val = self.tblHgt
                        self.tblHgt = 0
                        self.objDetails.arrReviews.add(objValue)
                        for index in 0..<self.objDetails.arrReviews.count
                        {
                            let objVal = self.objDetails.arrReviews[index] as! ReviewBO
                            let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: self.lblDesc.frame.size.width, font: self.lblDesc.font, string: objVal.reviewDesc)
                            
                            self.tblHgt += 198 + cellHgt - 25
                        }

                        self.tblViewReview.reloadData()
                        self.viewRateHgtConstraint.constant = 0
                        self.viewInnerRateHgtConstraint.constant = 0
                        self.viewRateTopConstraint.constant = 0
                        self.viewInnerRate.isHidden = true
                        self.viewRate.isHidden = true
                        self.contentVHgtConstraint.constant -= self.btnSubmit.frame.maxY + 10
                        let hgt = CGFloat(self.tblHgt)
                        self.tblViewHgtConstraints.constant = CGFloat(hgt)
                        self.viewReviewHgtConstraints.constant = CGFloat(55 + hgt)
                        self.tblViewReview.isHidden = false
                        self.lblNoData.isHidden = true
                        self.contentVHgtConstraint.constant += hgt - val
                        self.value = self.contentVHgtConstraint.constant
                    }
                }
            })
        }
    }

    @IBAction func onClickBtnRevealsReview(_ sender: UIButton) {
        
        sender.isSelected = !sender.isSelected
        let hgt: CGFloat = objDetails.arrReviews.count != 0 ? tblHgt : 45
        if sender.isSelected
        {
            tblViewHgtConstraints.constant = CGFloat(hgt)
            viewReviewHgtConstraints.constant = CGFloat(55 + hgt)
            tblViewReview.isHidden = objDetails.arrReviews.count != 0 ? false : true
            lblNoData.isHidden = objDetails.arrReviews.count != 0 ? true : false
            contentVHgtConstraint.constant += hgt
            value = contentVHgtConstraint.constant
            
        }
        else
        {
            viewReviewHgtConstraints.constant = 55
            tblViewHgtConstraints.constant = 0
            tblViewReview.isHidden = true
            lblNoData.isHidden = true
            contentVHgtConstraint.constant -= hgt
            value = contentVHgtConstraint.constant
            
        }


    }
    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return objDetails.arrReviews.count

    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        self.prototypeCell = tblViewReview.dequeueReusableCell(withIdentifier: "review_Cell")! as! ReviewTableViewCell
        
        let objVal = objDetails.arrReviews[indexPath.row] as! ReviewBO
        let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: prototypeCell.lblreview.frame.size.width, font: prototypeCell.lblreview.font, string: objVal.reviewDesc)
        objVal.cellHgt = cellHgt + 25
        return 198 + cellHgt - 25
        //        return 198
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "review_Cell", for: indexPath) as! ReviewTableViewCell
        let objVal = objDetails.arrReviews[indexPath.row] as! ReviewBO
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
                    objReview.review_for = "course"
                    objReview.for_id = self.objDetails.course_id
                    reviewVC.objReview = objReview
                    reviewVC.onClickUpdate = {(objRevVal) -> Void in
                        
                        self.objDetails.arrReviews.remove(objVal)
                        self.objDetails.arrReviews.add(objRevVal)
                        self.tblViewReview.reloadData()
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
    
    
    // MARK: -
    // MARK: - FloatRatingViewDelegate
    func floatRatingView(_ ratingView: FloatRatingView, didUpdate rating: Float)
    {
        
    }
    
    // MARK: -
    // MARK: - UITextViewDelegates
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtViewDesc.text == "Description"
        {
            txtViewDesc.text = ""
            txtViewDesc.textColor = .black
        }
    }
    func textViewDidEndEditing(_ textView: UITextView) {
        if txtViewDesc.text.isEmpty
        {
            txtViewDesc.text = "Description"
            txtViewDesc.textColor = .lightGray
            
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
    func textViewDynamicallyIncreaseSize() {
        let contentSize = self.txtViewDesc.sizeThatFits(self.txtViewDesc.bounds.size)
        let higntcons = contentSize.height < 55 ? 55 : contentSize.height
        txtDescHgtConstraint.constant = higntcons
        viewRateHgtConstraint.constant = (325 + higntcons) - 55
        contentVHgtConstraint.constant = value + higntcons
        print(value, higntcons)
        viewInnerRateHgtConstraint.constant = (270 + higntcons) - 55
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
