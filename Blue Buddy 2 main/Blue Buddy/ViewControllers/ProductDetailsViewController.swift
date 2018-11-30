//
//  ProductDetailsViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/4/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import FloatRatingView
class ProductDetailsViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UIScrollViewDelegate, UITextViewDelegate, FloatRatingViewDelegate, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    

    @IBOutlet weak var imgFeatured: UIImageView!

    @IBOutlet weak var lblPrice: UILabel!
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var imgCollectionView: UICollectionView!
    @IBOutlet weak var lablDesc: UILabel!
    @IBOutlet weak var btnMessage: UIButton!
    @IBOutlet weak var txtDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnSubmit: UIButton!
    @IBOutlet weak var viewRateTopConstraint: NSLayoutConstraint!
    @IBOutlet weak var btnMsgHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblNoData: UILabel!
    @IBOutlet weak var contentVHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewRateHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var lblEmail: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var tblViewHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewReviewHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var btnReview: UIButton!
    @IBOutlet weak var viewReview: UIView!
    @IBOutlet weak var viewInnerRateHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var txtReviewDesc: UITextView!
    @IBOutlet weak var txtTitle: UITextField!
    @IBOutlet weak var rating: FloatRatingView!
    @IBOutlet weak var btnRateNReview: UIButton!
    @IBOutlet weak var viewInnerRate: UIView!
    @IBOutlet weak var viewRate: UIView!
    @IBOutlet weak var viewContact: UIView!
    @IBOutlet weak var lblDesc: UILabel!
    @IBOutlet weak var viewDesc: UIView!
    @IBOutlet weak var pageCntrl: UIPageControl!
    @IBOutlet weak var productRate: FloatRatingView!
    @IBOutlet weak var contentV: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var lblProductName: UILabel!
    @IBOutlet weak var lblBadgeCount: UILabel!
    @IBOutlet weak var constraintHghtContactDetails: NSLayoutConstraint!
    
    var objProductDetails = ProductDetailsBO()
    var indexVal = Int()
    var tblHgt = CGFloat()
    var value = CGFloat()
    var prototypeCell = ReviewTableViewCell()
    var endDragging = false
    var scrollTimer = Timer()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        if objProductDetails.is_hidden {
            hideContactDetails()
        }
        
        let layout: UICollectionViewFlowLayout = UICollectionViewFlowLayout()
        layout.sectionInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        layout.minimumInteritemSpacing = 0
        layout.minimumLineSpacing = 0
        layout.scrollDirection = .horizontal
        imgCollectionView!.collectionViewLayout = layout
        imgCollectionView.delegate = self
        imgCollectionView.dataSource = self
        imgCollectionView.isPagingEnabled = true
        rating.delegate = self

        btnRateNReview.layer.cornerRadius = 5.0
        
        viewRateHgtConstraint.constant = 55
        viewReviewHgtConstraint.constant = 55
        
        let myColor = UIColor.lightGray
        txtReviewDesc.layer.cornerRadius = 5.0
        txtReviewDesc.layer.borderWidth = 1.0
        txtReviewDesc.layer.borderColor = myColor.cgColor
        
        txtTitle.layer.cornerRadius = 5.0
        txtTitle.layer.borderWidth = 1.0
        txtTitle.layer.borderColor = myColor.cgColor
        
        viewDesc.layer.cornerRadius = 5.0
        viewDesc.layer.borderWidth = 1.0
        viewDesc.layer.borderColor = myColor.cgColor
        
        viewContact.layer.cornerRadius = 5.0
        viewContact.layer.borderWidth = 1.0
        viewContact.layer.borderColor = myColor.cgColor
        
        viewRate.layer.cornerRadius = 5.0
        viewRate.layer.borderWidth = 1.0
        viewRate.layer.borderColor = myColor.cgColor
        
        viewReview.layer.cornerRadius = 5.0
        viewReview.layer.borderWidth = 1.0
        viewReview.layer.borderColor = myColor.cgColor
        lblPrice.layer.cornerRadius = lblPrice.frame.size.width/2
        lblPrice.clipsToBounds = true
        viewInnerRate.isHidden = true
        tblViewList.isHidden = true
        
        
        lblBadgeCount.layer.cornerRadius = lblBadgeCount.frame.size.height/2
        lblBadgeCount.clipsToBounds = true
        pageCntrl.numberOfPages = objProductDetails.arrImages.count
        
        let valHgt = APP_DELEGATE.heightWithConstrainedWidth(width: lblDesc.frame.width, font: lblDesc.font, string: objProductDetails.product_desc) + 25
        
        lblDesc.text = objProductDetails.product_desc
        lblName.text = objProductDetails.user_name.capitalized
        lblPrice.text = "$" + objProductDetails.price
        imgFeatured.isHidden = objProductDetails.is_featured ? false : true
        let boldAttributes = [NSForegroundColorAttributeName: lablDesc.textColor, NSFontAttributeName: lablDesc.font!] as [String : Any]
        let yourOtherAttributes = [NSForegroundColorAttributeName: lblEmail.textColor, NSFontAttributeName: lblEmail.font!] as [String : Any]
        
        let titleImgString = NSMutableAttributedString(string: "Email : ", attributes: boldAttributes)
        titleImgString.append(NSAttributedString(string: objProductDetails.user_email, attributes: yourOtherAttributes))
        lblEmail.attributedText = titleImgString
        
        let titleImgString1 = NSMutableAttributedString(string: "Call : ", attributes: boldAttributes)
        titleImgString1.append(NSAttributedString(string: objProductDetails.user_Phone, attributes: yourOtherAttributes))
        lblPhone.attributedText = titleImgString1
        
        lblProductName.text = objProductDetails.name.capitalized
        
        lblDescHgtConstraint.constant = valHgt < 45 ? 45 : valHgt
        viewDescHgtConstraint.constant = valHgt < 45 ? 45 + lablDesc.frame.maxY + 20 : valHgt + lablDesc.frame.maxY + 20
        productRate.rating = Float(objProductDetails.rating)
        
        btnRateNReview.layer.cornerRadius = 5.0
        btnReview.layer.cornerRadius = 5.0
        
        viewRateHgtConstraint.constant = 55
        viewReviewHgtConstraint.constant = 55
        
        if objProductDetails.user_id == LOGIN_CONSTANT.objProfile.userId
        {
            btnMsgHgtConstraint.constant = 0
            btnMessage.isHidden = true
        }
        if objProductDetails.is_reviewed
        {
            viewRateHgtConstraint.constant = 0
            viewRate.isHidden = true
            viewRateTopConstraint.constant = 0
        }
        
        lblNoData.isHidden = true
        for index in 0..<objProductDetails.arrReviews.count
        {
            let objVal = objProductDetails.arrReviews[index] as! ReviewBO
            let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: lblName.frame.size.width, font: lblName.font, string: objVal.reviewDesc)
            
            tblHgt += 198 + cellHgt - 25
        }
        contentVHgtConstraint.constant = viewContact.frame.maxY + 170 + valHgt
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

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        scrollTimer = Timer.scheduledTimer(timeInterval: 4.0, target: self, selector: #selector(self.scrollAutomatically), userInfo: nil, repeats: true)
    }
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        scrollTimer.invalidate()
    }
    
    
    
    // MARK: -
    // MARK: - UIButton Methods
    @IBAction func onClickRevealRateView(_ sender: UIButton) {
        
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
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        if indexVal == 1 || indexVal == 2
        {
            var isFound:Bool = false
            if let viewControllers = navigationController?.viewControllers {
                for viewController in viewControllers {
                    // some process
                    if viewController.isKind(of: indexVal == 1 ? BoatRentalViewController.self : MyListingViewController.self) {
                        isFound = true
                        if indexVal == 1 {
                            NotificationCenter.default.post(name: NSNotification.Name(rawValue: "updateProductList"), object: nil)
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

    @IBAction func onClickBtnReview(_ sender: UIButton) {
        sender.isSelected = !sender.isSelected
        let hgt: CGFloat = objProductDetails.arrReviews.count != 0 ? tblHgt : 45
        if sender.isSelected
        {
            tblViewHgtConstraint.constant = CGFloat(hgt)
            viewReviewHgtConstraint.constant = CGFloat(55 + hgt)
            tblViewList.isHidden = objProductDetails.arrReviews.count != 0 ? false : true
            lblNoData.isHidden = objProductDetails.arrReviews.count != 0 ? true : false
            contentVHgtConstraint.constant += hgt
            value = contentVHgtConstraint.constant
        }
        else
        {
            viewReviewHgtConstraint.constant = 55
            tblViewHgtConstraint.constant = 0
            tblViewList.isHidden = true
            lblNoData.isHidden = true
            contentVHgtConstraint.constant -= hgt
            value = contentVHgtConstraint.constant
        }
    }
    
    func hideContactDetails() {
        
        if true
        {
            let intRemoveHght = constraintHghtContactDetails.constant + 100
            contentVHgtConstraint.constant -= intRemoveHght
            constraintHghtContactDetails.constant = 0
            viewContact.isHidden = true
            value = contentVHgtConstraint.constant
        }
        else
        {
//            constraintHghtContactDetails.constant = 200
//            contentVHgtConstraint.constant += constraintHghtContactDetails.constant
//            viewContact.isHidden = false
//            value = contentVHgtConstraint.constant
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
            
        else if (txtReviewDesc.text == "Description") || (txtReviewDesc.text?.isEmpty)!
        {
            Common.showAlert(message: "Please enter your review description", title: "BLUE BUDDY", viewController: self)
        }
        else
        {
            let dictVal = NSMutableDictionary()
            dictVal["rev_title"] = txtTitle.text
            dictVal["rev_description"] = txtReviewDesc.text
            dictVal["rev_rating"] = String(rating.rating)
            dictVal["rev_for"] = "product"
            dictVal["for_id"] = objProductDetails.product_id
            MarketParser.callCreateReviewService(dictVal, complete: { (status, strMsg, objValue) in
                if status
                {
                    DispatchQueue.main.async {
                        self.objProductDetails.arrReviews.add(objValue)
                        let val = self.tblHgt
                        self.tblHgt = 0
                        for index in 0..<self.objProductDetails.arrReviews.count
                        {
                            let objVal = self.objProductDetails.arrReviews[index] as! ReviewBO
                            let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: self.lblDesc.frame.size.width, font: self.lblDesc.font, string: objVal.reviewDesc)
                            
                            self.tblHgt += 198 + cellHgt - 25
                        }
                        self.tblViewList.reloadData()
                        self.viewRateHgtConstraint.constant = 0
                        self.viewInnerRateHgtConstraint.constant = 0
                        self.viewRateTopConstraint.constant = 0
                        self.viewInnerRate.isHidden = true
                        self.viewRate.isHidden = true
                        self.contentVHgtConstraint.constant -= self.btnSubmit.frame.maxY + 10
                        self.tblViewHgtConstraint.constant = CGFloat(self.tblHgt)
                        self.viewReviewHgtConstraint.constant = CGFloat(55 + self.tblHgt)
                        self.tblViewList.isHidden = false
                        self.lblNoData.isHidden = true
                        self.contentVHgtConstraint.constant += self.tblHgt - val
                        self.value = self.contentVHgtConstraint.constant
                        
                    }
                }
            })
        }

    }
    @IBAction func onClickBtnMsg(_ sender: UIButton) {
        ProfileViewController.callChatDetails(objProductDetails.firebase_id, self)
    }
    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return objProductDetails.arrReviews.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let cell = tableView.dequeueReusableCell(withIdentifier: "review_Cell", for: indexPath) as! ReviewTableViewCell
        let objVal = objProductDetails.arrReviews[indexPath.row] as! ReviewBO
        cell.lblDate.text = objVal.date
        cell.lblName.text = objVal.user_name
        cell.lblTitle.text = objVal.review_title
        cell.lblreview.text = objVal.reviewDesc
        cell.viewRating.rating = Float(objVal.rating)!
        cell.lblDescHgtConstraint.constant = objVal.cellHgt
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
                    objReview.review_for = "product"
                    objReview.for_id = self.objProductDetails.product_id
                    reviewVC.objReview = objReview
                    reviewVC.onClickUpdate = {(objRevVal) -> Void in
                        
                        self.objProductDetails.arrReviews.remove(objVal)
                        self.objProductDetails.arrReviews.add(objRevVal)
                        self.tblViewList.reloadData()
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
        
        self.prototypeCell = tblViewList.dequeueReusableCell(withIdentifier: "review_Cell")! as! ReviewTableViewCell
        
        let objVal = objProductDetails.arrReviews[indexPath.row] as! ReviewBO
        let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: prototypeCell.lblreview.frame.size.width, font: prototypeCell.lblreview.font, string: objVal.reviewDesc)
        objVal.cellHgt = cellHgt + 25
        return 198 + cellHgt - 25
        //        return 198
    }
    
    
    // MARK: -
    // MARK: - UITextViewDelegates
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        if txtReviewDesc.text == "Description"
        {
            txtReviewDesc.text = ""
            txtReviewDesc.textColor = .black
        }
    }
    func textViewDidEndEditing(_ textView: UITextView) {
        if txtReviewDesc.text.isEmpty
        {
            txtReviewDesc.text = "Description"
            txtReviewDesc.textColor = .lightGray
            
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
    // MARK: - FloatRatingViewDelegate
    func floatRatingView(_ ratingView: FloatRatingView, didUpdate rating: Float)
    {
        
    }
    
    
    // MARK: -
    // MARK: - CollectionViewDelegates & Datasources
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: CGFloat(collectionView.frame.size.width), height: CGFloat(viewHeader.frame.size.height))
    }
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
   
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return objProductDetails.arrImages.count
    }
    
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "img_Cell",
                                                      for: indexPath) as! ImgCollectionViewCell
        
        if indexPath.row < objProductDetails.arrImages.count
        {
            let imgData: ImageBO = objProductDetails.arrImages[indexPath.row] as! ImageBO
            cell.imgProduct.kf.setImage(with: URL(string:imgData.image_name), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
        }
        else{
            cell.imgProduct.backgroundColor = .lightGray
        }
        
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        Common.displayImageLargerFromArray(viewController: self, arrImages: objProductDetails.arrImages, andIndex:indexPath.row)
       
    }

    // MARK: -
    // MARK: - UIScrollViewDelegate
    public func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if !decelerate {
            endScrolling(scrollView)
        } else {
            endDragging = true
        }
    }
    
    
    public func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        if endDragging {
            endDragging = false
            endScrolling(scrollView)
        }
    }
    
    

    // MARK: -
    // MARK: - Other Methods
    func textViewDynamicallyIncreaseSize() {
        let contentSize = self.txtReviewDesc.sizeThatFits(self.txtReviewDesc.bounds.size)
        let higntcons = contentSize.height < 55 ? 55 :contentSize.height
        txtDescHgtConstraint.constant = higntcons
        viewRateHgtConstraint.constant = (325 + higntcons) - 55
        contentVHgtConstraint.constant = value + higntcons
        viewInnerRateHgtConstraint.constant = (270 + higntcons) - 55
    }
    
    func scrollAutomatically(_ timer1: Timer) {
        
        if let coll  = imgCollectionView {
            for cell in coll.visibleCells {
                let indexPath: IndexPath? = coll.indexPath(for: cell)
                if ((indexPath?.row)!  < objProductDetails.arrImages.count - 1){
                    let indexPath1: IndexPath?
                    indexPath1 = IndexPath.init(row: (indexPath?.row)! + 1, section: (indexPath?.section)!)
                    
                    coll.scrollToItem(at: indexPath1!, at: .right, animated: true)
                    self.pageCntrl.currentPage = indexPath1!.row
                }
                else{
                    let indexPath1: IndexPath?
                    indexPath1 = IndexPath.init(row: 0, section: (indexPath?.section)!)
                    coll.scrollToItem(at: indexPath1!, at: .left, animated: true)
                    self.pageCntrl.currentPage = indexPath1!.row
                }
            }
        }
    }

    fileprivate func endScrolling(_ scrollView: UIScrollView) {
        
        /**
         end scrolling
         */
        let width = scrollView.bounds.width
        let page = (scrollView.contentOffset.x + (0.5 * width)) / width
       // currentIndex = Int(page)
        pageCntrl.currentPage = Int(page)
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
