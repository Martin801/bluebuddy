//
//  MyListingViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/21/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit


class MyListingViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {

    @IBOutlet weak var btnAdd: UIButton!
    @IBOutlet weak var tblList: UITableView!
    @IBOutlet weak var lblNotifications: UILabel!
    var arrMyListing = NSMutableArray()
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.bringSubview(toFront: btnAdd)
        lblNotifications.layer.cornerRadius = lblNotifications.frame.size.height/2
        lblNotifications.clipsToBounds = true
        tblList.register(UINib.init(nibName: "ServiceTableViewCell", bundle: Bundle.main), forCellReuseIdentifier: "service_cell")
        tblList.register(UINib.init(nibName: "MyListingServiceTableViewCell", bundle: Bundle.main), forCellReuseIdentifier: "myListing_Cell")
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

        self.callMyListVC()
        NotificationCenter.default.addObserver(self, selector: #selector(MyListingViewController.callMyListVC), name: NSNotification.Name(rawValue: "updateMyList"), object: nil)

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    // MARK: -
    // MARK: - UIButton Action
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }

    @IBAction func onClickBtnNotifications(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    
    @IBAction func onClickBtnAdd(_ sender: UIButton) {
        let listVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "MarketViewController") as! MarketViewController
        listVC.indexVal = 2
        self.navigationController?.pushViewController(listVC, animated: true)

    }
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    
    func numberOfSections(in tableView: UITableView) -> Int
    {
        return arrMyListing.count
    }
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 45
    }
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView()
        let lblTitle = UILabel()
        let myColor = UIColor.init(colorLiteralRed: 0/255.0, green: 56/255.0, blue: 118/255.0, alpha: 1.0)
        lblTitle.frame = CGRect( x: 0, y: 0, width: self.tblList.frame.size.width, height: 45)
        lblTitle.textColor = myColor
        let objList = arrMyListing[section] as! MyListingBO
        lblTitle.text = objList.section_Name
        headerView.backgroundColor = .white
        lblTitle.textAlignment = .center
        headerView.addSubview(lblTitle)
        headerView.layer.borderWidth = 2.0
        headerView.layer.borderColor = myColor.cgColor
        headerView.layer.cornerRadius = 8.0
        return headerView
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let objList = arrMyListing[indexPath.section] as! MyListingBO
        switch objList.type {
        case 1:
            return 177
        case 2:
            return 205
        default:
            break
        }
        return 0
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        let objList = arrMyListing[section] as! MyListingBO
       
        return objList.arrValue.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        
        let objList = arrMyListing[indexPath.section] as! MyListingBO
        switch objList.type {
        case 1:
            var cell:MyListingServiceTableViewCell? = tableView.dequeueReusableCell(withIdentifier: "myListing_Cell") as? MyListingServiceTableViewCell
            if (cell == nil)
            {
                let nib:NSArray = Bundle.main.loadNibNamed("myListing_Cell", owner: self, options: nil)! as NSArray
                cell = nib[0] as? MyListingServiceTableViewCell
            }
            
            cell?.imgBG.layer.borderWidth = 0.5
            cell?.imgBG.layer.borderColor = UIColor.lightGray.cgColor
            cell?.imgBG.layer.cornerRadius = 8.0
            cell?.imgBG.layer.shadowColor = UIColor.lightGray.cgColor
            cell?.lblPrice.layer.cornerRadius = (cell?.lblPrice.frame.size.height)!/2
            cell?.lblPrice.clipsToBounds = true
            cell?.imgViewPic.layer.cornerRadius = 5.0
            cell?.imgViewPic.clipsToBounds = true
            cell?.btnDetails.addTarget(self, action: #selector(self.detailBtnPressed(_:)), for: .touchUpInside)
            cell?.btnEdit.addTarget(self, action: #selector(self.detailBtnPressed(_:)), for: .touchUpInside)
            if objList.section_Name == "COURSE"
            {
                let courseData = objList.arrValue[indexPath.row] as! CoursesDetailsBO
                cell?.lblName.text = courseData.category.capitalized
                cell?.lblAddress.text = courseData.user_loc
                cell?.lblPostedBy.text = "Posted by: " + courseData.user_name
                cell?.lblPrice.text = "$" + courseData.price
                cell?.imgViewPic.kf.setImage(with: URL(string:courseData.course_image), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
                cell?.imgFeatured.isHidden = courseData.is_featured ? false : true
                return cell!
            }
            else if objList.section_Name == "PRODUCT"
            {
                let productData =  objList.arrValue[indexPath.row] as! ProductDetailsBO
                cell?.lblName.text = productData.name.capitalized
                cell?.lblAddress.text = productData.user_loc
                cell?.lblPostedBy.text = "Posted by: " + productData.user_name
                cell?.lblPrice.text = "$" + productData.price
                cell?.imgViewPic.kf.setImage(with: URL(string:productData.arrImages[0] as! String), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
                cell?.imgFeatured.isHidden = productData.is_featured ? false : true
                return cell!

                }
            else
            {
                let charterData =  objList.arrValue[indexPath.row] as! CharterDetailsBO
                cell?.lblName.text = charterData.name.capitalized
                cell?.lblAddress.text = charterData.user_loc
                cell?.lblPostedBy.text = "Posted by: " + charterData.user_name
                cell?.lblPrice.text = "$" + charterData.price
                cell?.imgViewPic.kf.setImage(with: URL(string:charterData.charter_image), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
                cell?.imgFeatured.isHidden = charterData.is_featured ? false : true
                
                return cell!
            }
        case 2:
            var cell:ServiceTableViewCell? = tableView.dequeueReusableCell(withIdentifier: "service_cell") as? ServiceTableViewCell
            if (cell == nil)
            {
                let nib:NSArray = Bundle.main.loadNibNamed("service_cell", owner: self, options: nil)! as NSArray
                cell = nib[0] as? ServiceTableViewCell
            }
            let objService = objList.arrValue[indexPath.row] as! ServiceDetailsBO
            cell?.imgBG.layer.borderWidth = 0.5
            cell?.imgBG.layer.borderColor = UIColor.lightGray.cgColor
            cell?.imgBG.layer.cornerRadius = 8.0
            cell?.imgBG.layer.shadowColor = UIColor.lightGray.cgColor
            cell?.lblEmail.text = objService.user_email
            cell?.lblPhone.text = objService.user_Phone
            cell?.servicePostedBy.text = objService.user_name
            let boldAttributes = [NSForegroundColorAttributeName: cell?.lblPhone.textColor as Any, NSFontAttributeName: cell?.serviceName.font! as Any] as [String : Any]
            let yourOtherAttributes = [NSForegroundColorAttributeName: cell?.lblPhone.textColor as Any, NSFontAttributeName: cell?.lblPhone.font! as Any] as [String : Any]
            
            let titleImgString = NSMutableAttributedString(string: "Service Offered : ", attributes: boldAttributes)
            titleImgString.append(NSAttributedString(string: objService.service_type, attributes: yourOtherAttributes))
            cell?.serviceName.attributedText = titleImgString
            cell?.imgService.layer.cornerRadius = 5.0
            cell?.imgService.clipsToBounds = true
            cell?.serviceRate.rating = Float(objService.rating)!
            cell?.imgService.kf.setImage(with: URL(string:objService.service_image), placeholder: nil, options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
            cell?.imgFeatured.isHidden = objService.is_featured ? false : true
            if objService.is_editable
            {
                cell?.btnEdit.isHidden = false
                cell?.btnEdit.setTitle("EDIT", for: .normal)
            }
            else
            {
                cell?.btnEdit.isHidden = false
                cell?.btnEdit.setTitle("CHAT", for: .normal)
            }
            cell?.onClickDetails = {() -> Void in
                 self.callDetailsfrom(false, objService, objList.arrValue)
            }
            cell?.onClickOtherBtn = {() -> Void in
                 self.callDetailsfrom(true, objService, objList.arrValue)
            }
            cell?.btnFlag.isHidden = true
            cell?.imgFlag.isHidden = true
            return cell!
            
        default:
            break
            
            
        }
        return UITableViewCell()
    }
    // MARK: -
    // MARK: - UITableViewCell Button Action
    func detailBtnPressed (_ sender: UIButton) {
        var specific_id = String()
        var specific_type = String()
        let value = sender.tag == 1 ? true : false
        if let superview = sender.superview, let cell = superview.superview as? MyListingServiceTableViewCell {
            if let indexPath = tblList.indexPath(for: cell) {
                let item = arrMyListing[indexPath.section] as! MyListingBO
                if item.section_Name == "COURSE"
                {
                    let courseData = item.arrValue[indexPath.row] as! CoursesDetailsBO
                    specific_id = courseData.course_id
                    specific_type = "1"
                }
                else if item.section_Name == "PRODUCT"
                {
                    let productData = item.arrValue[indexPath.row] as! ProductDetailsBO
                    specific_id = productData.product_id
                    specific_type = "2"
                }
                else{
                    let charterData =  item.arrValue[indexPath.row] as! CharterDetailsBO
                    specific_id = charterData.charter_id
                    specific_type = "3"
                }
                switch specific_type {
                case "1":
                    self.callCourseDetailsfrom(value ? false : true, specific_id)
                case "2":
                    self.callProdctDetailsService(value ? false : true, specific_id as NSString)
                case "3":
                    self.callCharterDetailsService(strId: specific_id as NSString, isFromDetails: value ? true :false)
                default:
                    break
                }
                print(item)
            }
        }
        // you can access controller here by using self
        
    }
    
    // MARK: -
    // MARK: - Other Methods
    func callDetailsfrom( _ isFromEdit :Bool, _ objValue: ServiceDetailsBO, _ arrList :NSMutableArray)
    {
        MarketParser.callServiceDetailsWebService(["id" : objValue.service_id], complete: { (status, strMsg, objServiceDetails) in
            if status
            {
                DispatchQueue.main.async {
                    if !isFromEdit
                    {
                        let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ServiceDetailsViewController") as! ServiceDetailsViewController
                        detailsVC.objServiceDetails = objServiceDetails
                        self.navigationController?.pushViewController(detailsVC, animated: true)
                    }
                    else
                    {
                        let updateVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddServiceViewController") as! AddServiceViewController
                        updateVC.indexVal = 2
                        updateVC.objServiceData = objServiceDetails
                        updateVC.onDeletingService = {() -> Void in
                            arrList.remove(objValue)
                            self.tblList.reloadData()
                        }
                        self.navigationController?.pushViewController(updateVC, animated: true)
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
    
    func callCharterDetailsService(strId: NSString, isFromDetails: Bool )
    {
        MarketParser.callChaterDetailsService(["id" : strId], complete: { (status, strMsg, objDetails) in
            if status
            {
                DispatchQueue.main.async {
                    if isFromDetails
                    {
                        let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "RentalDetailsViewController") as! RentalDetailsViewController
                        detailsVC.objCharterDetails = objDetails
                        detailsVC.indexVal = 0
                        self.navigationController?.pushViewController(detailsVC, animated: true)
                    }
                    else{
                        let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "UploadPicViewController") as! UploadPicViewController
                        uploadVC.index = 1
                        uploadVC.value = 2
                        uploadVC.charterDetails = objDetails
                        self.navigationController?.pushViewController(uploadVC, animated: true)
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
    
    func callCourseDetailsfrom( _ isFromEdit :Bool, _ strId: String)
    {
        MarketParser.callCourseDetailsService(["id" : strId], complete: { (status, strMsg, objCourseDetails) in
            if status
            {
                DispatchQueue.main.async {
                    if !isFromEdit
                    {
                        let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "CourseDetailsViewController") as! CourseDetailsViewController
                        let objCourse = objCourseDetails
                        objCourse.course_id = strId
                        detailsVC.objDetails = objCourse
                        self.navigationController?.pushViewController(detailsVC, animated: true)
                    }
                    else
                    {
                        let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "UploadPicViewController") as! UploadPicViewController
                        uploadVC.index = 2
                        uploadVC.value = 2
                        let objCourse = objCourseDetails
                        objCourse.course_id = strId
                        uploadVC.courseDetails = objCourse
                        self.navigationController?.pushViewController(uploadVC, animated: true)
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
    
    func callProdctDetailsService(_ isFromEdit :Bool, _ strId: NSString)
    {
        MarketParser.callProductDetailsWebService(["id" : strId], complete: { (status, strMsg, objDetails) in
            if status
            {
                DispatchQueue.main.async {
                    if isFromEdit
                    {
                        let uploadVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "MultipleUploadViewController") as! MultipleUploadViewController
                        let objValue = objDetails
                        objValue.product_id = strId as String
                        uploadVC.productData = objValue
                        uploadVC.indexVal = 2
                        self.navigationController?.pushViewController(uploadVC, animated: true)
                    }
                    else{
                        let detailsVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ProductDetailsViewController") as! ProductDetailsViewController
                        let objValue = objDetails
                        objValue.product_id = strId as String
                        detailsVC.objProductDetails = objValue
                        self.navigationController?.pushViewController(detailsVC, animated: true)
                    }
                }
            }
        })
    }


    @objc private func callMyListVC()
    {
        UserParser.callMyListingService([ : ]) { (status, strMsg, arrList) in
            if status
            {
                self.arrMyListing = arrList as! NSMutableArray
                self.tblList.reloadData()
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
