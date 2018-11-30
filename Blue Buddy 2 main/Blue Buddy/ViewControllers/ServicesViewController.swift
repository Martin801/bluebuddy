//
//  ServicesViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/1/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import GooglePlacePicker
import MarqueeLabel
class ServicesViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate, GMSPlacePickerViewControllerDelegate {

    @IBOutlet weak var tblViewHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var viewLocHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var txtLocation: UITextField!
    @IBOutlet weak var viewSlider: UISlider!
    @IBOutlet weak var lblSliderVal: UILabel!
    @IBOutlet weak var viewOtherLoc: UIView!
    @IBOutlet weak var viewCurrLoc: UIView!
    @IBOutlet weak var viewLoc: UIView!
    @IBOutlet weak var viewBtn: UIView!
    @IBOutlet weak var lblHeader: UILabel!
    @IBOutlet weak var btnOtherLoc: UIButton!
    @IBOutlet weak var btnCurrentLoc: UIButton!
    @IBOutlet weak var btnAdd: UIButton!
    @IBOutlet weak var lblBagdeNo: UILabel!
    @IBOutlet weak var tblViewList: UITableView!
    var strAddress = String()
    var placeCoord: CLLocationCoordinate2D?
    var lengthyLabel: MarqueeLabel?
    var isCurrentLoc = Bool()
    var selectedIndex: Int = 0
    var tblconst = CGFloat()
    var timer = Timer()
    var arrServiceList = NSMutableArray()
    override func viewDidLoad() {
        super.viewDidLoad()

        let myColor = UIColor.darkGray
        btnOtherLoc.layer.borderColor         = myColor.cgColor
        btnOtherLoc.layer.borderWidth         = 1.0
        btnOtherLoc.layer.cornerRadius        = 5.0
        btnCurrentLoc.layer.borderColor             = myColor.cgColor
        btnCurrentLoc.layer.borderWidth             = 1.0
        btnCurrentLoc.layer.cornerRadius            = 5.0
        lblBagdeNo.layer.cornerRadius = lblBagdeNo.frame.size.height/2
        lblBagdeNo.clipsToBounds = true
        btnCurrentLoc.layer.cornerRadius = 5.0
        btnOtherLoc.layer.cornerRadius = 5.0
        viewCurrLoc.isHidden = true
        viewOtherLoc.isHidden = true
        lengthyLabel = MarqueeLabel.init(frame: txtLocation.frame, duration: 8.0, fadeLength: 10.0)
        lengthyLabel?.textColor = UIColor.darkGray
        lengthyLabel?.type = .continuous
        tblViewHgtConstraint.constant = view.frame.size.height - (viewBtn.frame.maxY + 65)
        tblViewList.register(UINib.init(nibName: "ServiceTableViewCell", bundle: Bundle.main), forCellReuseIdentifier: "service_cell")
        DispatchQueue.global().async {
            TripsParser.callNotificationCountService([ : ]) { (status, strCounter) in
                if status
                {
                    DispatchQueue.main.async {
                        self.lblBagdeNo.text = strCounter
                    }
                }
            }
        }

        self.callServiceListWebservice()
        NotificationCenter.default.addObserver(self, selector: #selector(ServicesViewController.callServiceListWebservice), name: NSNotification.Name(rawValue: "updateServiceList"), object: nil)

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onClickBtnHeaderSelection(_ sender: UIButton) {
        
        sender.isSelected = !sender.isSelected
        if sender.isSelected
        {
            tblViewHgtConstraint.constant = view.frame.size.height - (viewLoc.frame.maxY + 65)
            switch sender.tag {
            case 1:
                
                selectedIndex = 1
                viewLoc.isHidden = false
                self.btnCurrentLoc.setTitleColor(.white, for: .selected)
                self.btnCurrentLoc.backgroundColor = UIColor.init(red: 0/255.0, green: 56/255.0, blue: 118/255.0, alpha: 1.0)
                self.btnOtherLoc.isSelected = false
                viewSlider.value = 1
                lblSliderVal.text = "1" + "km"
                self.btnOtherLoc.setTitleColor(.black, for: .normal)
                self.btnOtherLoc.backgroundColor = .white
                self.viewCurrLoc.isHidden = false
                self.viewOtherLoc.isHidden = true
                viewLocHgtConstraint.constant = 65
                self.callServiceListWebservice()
            
                self.view.bringSubview(toFront: self.viewCurrLoc)
                
            case 2:
                selectedIndex = 2
                viewLoc.isHidden = false
                self.btnCurrentLoc.isSelected = false
                self.btnOtherLoc.setTitleColor(.white, for: .selected)
                self.btnOtherLoc.backgroundColor = UIColor.init(red: 0/255.0, green: 56/255.0, blue: 118/255.0, alpha: 1.0)
                self.btnCurrentLoc.setTitleColor(.black, for: .normal)
                self.btnCurrentLoc.backgroundColor = .white
                self.viewOtherLoc.isHidden = false
                self.viewCurrLoc.isHidden = true
                viewLocHgtConstraint.constant = 65
                self.view.bringSubview(toFront: self.viewOtherLoc)
                
            default:
                selectedIndex = 0
                tblViewHgtConstraint.constant = view.frame.size.height - (viewLoc.frame.maxY)
                viewLoc.isHidden = true
                self.btnCurrentLoc.isSelected = false
                self.btnOtherLoc.isSelected = false
                
                self.btnOtherLoc.setTitleColor(.black, for: .normal)
                self.btnOtherLoc.backgroundColor = .white
                self.btnCurrentLoc.setTitleColor(.black, for: .normal)
                self.btnCurrentLoc.backgroundColor = .white
                self.viewOtherLoc.isHidden = true
                self.viewCurrLoc.isHidden = true
                viewLocHgtConstraint.constant = 0
                self.callServiceListWebservice()
                
                break
            }
        }
        else
        {
            tblViewHgtConstraint.constant = view.frame.size.height - viewLoc.frame.maxY
            selectedIndex = 0
            viewLoc.isHidden = true
            self.btnCurrentLoc.isSelected = false
            self.btnOtherLoc.isSelected = false
            self.btnOtherLoc.setTitleColor(.black, for: .normal)
            self.btnOtherLoc.backgroundColor = .white
            self.btnCurrentLoc.setTitleColor(.black, for: .normal)
            self.btnCurrentLoc.backgroundColor = .white
            self.viewOtherLoc.isHidden = true
            self.viewCurrLoc.isHidden = true
            viewLocHgtConstraint.constant = 0
            self.callServiceListWebservice()
        }

    }
    
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func onChangeSliderValue(_ sender: UISlider) {
        
        let value = Int (sender.value)
        lblSliderVal.text = String (value) + " km"
        selectedIndex = 1
        NSObject.cancelPreviousPerformRequests(withTarget: self, selector: #selector(self.callServiceListWebservice), object: nil)
        if (timer.isValid) {
            timer.invalidate()
        }
        timer = Timer(timeInterval: 0.9, target: self, selector: #selector(self.callServiceListWebservice), userInfo: nil, repeats: false)
        RunLoop.main.add(timer, forMode: RunLoopMode.defaultRunLoopMode)
    }
    @IBAction func onClickBtnAdd(_ sender: UIButton) {
        DispatchQueue.main.async {
            let addServiceVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "AddServiceViewController") as! AddServiceViewController
            addServiceVC.indexVal = 1
            self.navigationController?.pushViewController(addServiceVC, animated: true)
        }
    }
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
//        APP_DELEGATE.navController?.pushViewController(notificationVC, animated: true)
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    
    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return arrServiceList.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        var cell:ServiceTableViewCell? = tableView.dequeueReusableCell(withIdentifier: "service_cell") as? ServiceTableViewCell
        if (cell == nil)
        {
            let nib:NSArray = Bundle.main.loadNibNamed("service_cell", owner: self, options: nil)! as NSArray
            cell = nib[0] as? ServiceTableViewCell
        }
        let objService = arrServiceList[indexPath.row] as! ServiceDetailsBO
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
//            cell.imgFlag.isHidden = false
//            cell.btnFlag.isHidden = false
//            cell.imgFlag.image = objValue.is_flagged ? #imageLiteral(resourceName: "report_flag.png") : #imageLiteral(resourceName: "flag.png")
        }
        
        cell?.lblEmail.isHidden = objService.is_hidden
        cell?.lblPhone.isHidden = objService.is_hidden
//        cell?.imgVwEmail.isHidden = objService.is_hidden
//        cell?.imgVwPhone.isHidden = objService.is_hidden
        
        cell?.onClickDetails = {() -> Void in
            self.callDetailsfrom(false, objService)
        }
        cell?.onClickOtherBtn = {() -> Void in
            if objService.is_editable
            {
                self.callDetailsfrom(true, objService)
            }
            else{
                ProfileViewController.callChatDetails(objService.firebase_id, self)
            }
        }

        
        cell?.onClickReport = {() -> Void in
            
            let alert = UIAlertController(title: "BLUE BUDDY", message: "Are you sure you want to report?", preferredStyle: UIAlertControllerStyle.alert)
            let okAction = UIAlertAction(title: NSLocalizedString("YES", comment: "Ok action"), style: .default, handler: {(_ action: UIAlertAction) -> Void in
                let dictVal = NSMutableDictionary()
                dictVal["type"] = "service"
                dictVal["type_id"] = objService.service_id
                TripsParser.callReportService(dictVal, complete: { (status, strMsg) in
                    if status
                    {
                        cell?.imgFlag.image = #imageLiteral(resourceName: "report_flag.png")
                        objService.is_flagged = true
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                    else{
                        Common.showAlert(message: strMsg, title: "BLUE BUDDY", viewController: self)
                    }
                    
                })
            })
            let cancelAction = UIAlertAction(title: "NO", style: .cancel, handler: nil)
            alert.addAction(okAction)
            alert.addAction(cancelAction)
            self.present(alert, animated: true, completion: nil)
        }

        return cell!
    }
    
    
    // MARK: -
    // MARK: - UITextFeildDelegate Methods
    
    func textFieldDidBeginEditing(_ textField: UITextField)
    {
        let center = CLLocationCoordinate2D(latitude: APP_DELEGATE.currLoc.coordinate.latitude, longitude: APP_DELEGATE.currLoc.coordinate.longitude)
        let northEast = CLLocationCoordinate2D(latitude: center.latitude + 0.001,
                                               longitude: center.longitude + 0.001)
        let southWest = CLLocationCoordinate2D(latitude: center.latitude - 0.001,
                                               longitude: center.longitude - 0.001)
        let viewport = GMSCoordinateBounds(coordinate: northEast, coordinate: southWest)
        let config = GMSPlacePickerConfig(viewport: viewport)
        let placePicker = GMSPlacePickerViewController(config: config)
        placePicker.delegate = self
        present(placePicker, animated: true, completion: nil)
    }
    
    
    // MARK: -
    // MARK: - GMSPlacePickerViewControllerDelegate Methods
    func placePicker(_ viewController: GMSPlacePickerViewController, didPick place: GMSPlace) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        print("Place name \(place.name)")
        print("Place address \(place.formattedAddress ?? "")")
        print("Place attributions \(String(describing: place.attributions))")
        print("Coordinates::\(place.coordinate.latitude)")
        placeCoord = place.coordinate
        txtLocation.text = " "
        txtLocation.placeholder = nil
        txtLocation.leftView = lengthyLabel
        txtLocation.leftViewMode = .always
        lengthyLabel?.text = place.formattedAddress
        if place.formattedAddress != nil {
            strAddress = place.formattedAddress!
        }
        else {
            Common.getAddressFromCurrentLatLong { (strAddresReturned) in
                self.strAddress = strAddresReturned
                self.lengthyLabel?.text = strAddresReturned
            }
        }
        self.callServiceListWebservice()
        
        //        self.callCharterListVC(strLat: String(latitude) as NSString, strLong: String(longitude) as NSString, strRadius: "50", valueFrom: 1)
    }
    
    func placePickerDidCancel(_ viewController: GMSPlacePickerViewController) {
        // Dismiss the place picker, as it cannot dismiss itself.
        viewController.dismiss(animated: true, completion: nil)
        
        print("No place selected")
    }

    func callServiceListWebservice()
    {
        let dictVal = NSMutableDictionary()
        switch selectedIndex {
        case 0:
            dictVal[""] = ""
        case 1:
            dictVal["user_lat"] = String(APP_DELEGATE.currLoc.coordinate.latitude) as NSString
            dictVal["radius"] = String(viewSlider.value)
            dictVal["user_long"] = String(APP_DELEGATE.currLoc.coordinate.longitude) as NSString
        case 2:
            let latitude = Double((placeCoord?.latitude)!)
            let longitude = Double((placeCoord?.longitude)!)
            dictVal["user_lat"] = String(latitude)
            dictVal["radius"] = "50"
            dictVal["user_long"] = String(longitude)
            
        default:
            break
        }
        MarketParser.callServiceListService(dictVal, complete: { (status, strMsg, arrValue) in
            if status
            {
                DispatchQueue.main.async {
                    self.arrServiceList.removeAllObjects()
                    self.arrServiceList = arrValue as! NSMutableArray
                    self.tblViewList.reloadData()
                    self.tblViewList.isHidden = false
//                    self.lblNoData.isHidden = true
                }
            }
            else
            {
                DispatchQueue.main.async {
                    self.arrServiceList.removeAllObjects()
                    self.tblViewList.reloadData()
                    self.tblViewList.isHidden = true
//                    self.lblNoData.isHidden = false
                }
            }
        })
    }


    func callDetailsfrom( _ isFromEdit :Bool, _ objValue: ServiceDetailsBO)
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
                        updateVC.objServiceData = objServiceDetails
                        updateVC.onDeletingService = {() -> Void in
                            self.arrServiceList.remove(objValue)
                            self.tblViewList.reloadData()
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

    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
