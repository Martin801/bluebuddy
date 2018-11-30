//
//  ChatListViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/8/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import Firebase
import NVActivityIndicatorView
import AudioToolbox
class ChatListViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    var arrList = [Conversation]()
    var arrUserList = [User]()
    var selectedIndex = 2
    @IBOutlet weak var lblNotifications: UILabel!
    @IBOutlet weak var lblLine1: UILabel!
    @IBOutlet weak var lblLine2: UILabel!
    @IBOutlet weak var btnUser: UIButton!
    @IBOutlet weak var tblChatList: UITableView!
    @IBOutlet weak var tbListUser: UITableView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var btnChatList: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        lblNotifications.layer.cornerRadius = lblNotifications.frame.size.height/2
        lblNotifications.clipsToBounds = true
        lblLine2.isHidden = true
        btnChatList.titleLabel?.textColor = .darkGray
        self.fetchAllUsers()
        self.fetchData()
//        let exceptID = Auth.auth().currentUser?.uid
//        Database.database().reference().child("users").observe(.childAdded, with: { (snapshot) in
//            let id = snapshot.key
//            let data = snapshot.value as! [String: Any]
//            let credentials = data["credentials"] as! [String: String]
//            if id != exceptID {
//                let objPro = FriendBO()
//                objPro.name = credentials["name"]!
//                objPro.email = credentials["email"]!
//                objPro.image = credentials["image"]!
//                objPro.fid = id
//                self.arrUserList.append(objPro)
//                self.tbListUser.reloadData()
//            }
//        })
//           Database.database().reference().child("users").child(exceptID!).child("conversations").observe(.childAdded, with: { (snapshot) in
//                if snapshot.exists() {
//                    let fromID = snapshot.key
//                    let values = snapshot.value as! [String: String]
//                    let location = values["location"]!
//                    self.conversationList(location as NSString, fromID as NSString, exceptID!)
//            }
//        })
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
//        let btn = UIButton()
//        btn.tag = 1
//        onClickBtnSlider(btn)
        self.scrollViewMain.contentOffset.x = CGFloat(0)
        self.tblChatList.reloadData()
        self.tblChatList.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    //Downloads users list for Contacts View
    func fetchAllUsers()  {
        let activityData = ActivityData()
        NVActivityIndicatorView.DEFAULT_TYPE = .ballClipRotateMultiple
        DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
            NVActivityIndicatorPresenter.sharedInstance.startAnimating(activityData)
        }
        
        if let id = Auth.auth().currentUser?.uid {
            User.downloadAllUsers(exceptID: id, completion: {(user) in
                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                    NVActivityIndicatorPresenter.sharedInstance.stopAnimating()
                }
                DispatchQueue.main.async {
                    self.arrUserList.append(user)
                    self.tbListUser.reloadData()
                }
            })
        }
    }
    
    
    @IBAction func onClickBtnNotification(_ sender: UIButton) {
        let notificationVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "NotificationViewController") as! NotificationViewController
        self.navigationController?.pushViewController(notificationVC, animated: true)
    }
    
    func fetchData() {
        Conversation.showConversations { (conversations) in
            self.arrList = conversations
            self.arrList.sort{ ($0).lastMessage.timestamp > $1.lastMessage.timestamp }
            DispatchQueue.main.async {
                self.tblChatList.reloadData()
                for conversation in self.arrList {
                    if (conversation).lastMessage.isRead == false {
                        self.playSound()
                        break
                    }
                }
            }
        }
    }
    func playSound()  {
        var soundURL: NSURL?
        var soundID:SystemSoundID = 0
        let filePath = Bundle.main.path(forResource: "newMessage", ofType: "wav")
        soundURL = NSURL(fileURLWithPath: filePath!)
        AudioServicesCreateSystemSoundID(soundURL!, &soundID)
        AudioServicesPlaySystemSound(soundID)
    }
    @IBAction func onClickBtnSlider(_ sender: UIButton) {
        DispatchQueue.main.async {
            UIView.animate(withDuration: 0.2, delay: 0, options: UIViewAnimationOptions.curveEaseOut, animations: {
                self.scrollViewMain.contentOffset.x = CGFloat(self.scrollViewMain.frame.size.width)
                if  sender.tag == 1 {
                    self.btnUser.titleLabel?.textColor = .black
                    self.btnChatList.titleLabel?.textColor = .darkGray
                    self.lblLine1.isHidden = false
                    self.lblLine2.isHidden = true
                    self.selectedIndex = 1
                    self.scrollViewMain.contentOffset.x = CGFloat(0)
                    self.tbListUser.reloadData()
                    self.tbListUser.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
                }
                else{
                    self.btnChatList.titleLabel?.textColor = .darkGray
                    self.btnUser.titleLabel?.textColor = .black
                    self.lblLine1.isHidden = true
                    self.lblLine2.isHidden = false
                    self.selectedIndex = 2
                    self.scrollViewMain.contentOffset.x = CGFloat(self.scrollViewMain.frame.size.width)
                    self.tblChatList.reloadData()
                    self.tblChatList.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
                }
            }, completion: nil)
        }
    }
    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if tableView == tbListUser
        {
            return 95
        }
        else
        {
            return 120
        }
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if tableView == tbListUser
        {
            return arrUserList.count
        }
        else{
            return arrList.count
        }
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        if tableView == tbListUser
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "chatList_Cell", for: indexPath) as! ChatListTableViewCell
            cell.imgBg.layer.borderWidth = 0.5
            cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
            cell.imgBg.layer.cornerRadius = 8.0
            cell.imgBg.layer.shadowColor = UIColor.lightGray.cgColor
            cell.lblName.text = self.arrUserList[indexPath.row].name.capitalized
            cell.lblInitial.layer.cornerRadius = cell.lblInitial.frame.size.width/2
            cell.lblInitial.clipsToBounds = true
            let val = self.arrUserList[indexPath.row].name
            let firstL = val[val.startIndex]
            cell.lblInitial.text = String(firstL).capitalized
            return cell
        }
        else if tableView == tblChatList
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "existingChatListCell", for: indexPath) as! ExistingChatTableViewCell
         //   cell.clearCellData()
            cell.imgBG.layer.borderWidth = 0.5
            cell.imgBG.layer.borderColor = UIColor.lightGray.cgColor
            cell.imgBG.layer.cornerRadius = 8.0
            cell.imgBG.layer.shadowColor = UIColor.lightGray.cgColor
            cell.imgProfile.layer.borderWidth = 0.5
            cell.imgProfile.layer.borderColor = UIColor.lightGray.cgColor
            cell.imgProfile.layer.cornerRadius = cell.imgProfile.frame.size.height/2
            cell.imgProfile.clipsToBounds = true
          //  cell.clearCellData()
            if self.arrList[indexPath.row].user.profilePic != "default"
            {
                cell.imgProfile.kf.setImage(with: URL(string:self.arrList[indexPath.row].user.profilePic), placeholder: #imageLiteral(resourceName: "user.png"), options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
            }
            else {
                cell.imgProfile.image = UIImage(named: "default_user")
            }
         //   cell.imgProfile.image = self.arrList[indexPath.row].user.profilePic
            cell.lblName.text = self.arrList[indexPath.row].user.name.capitalized
            cell.lblMsg.text = self.arrList[indexPath.row].lastMessage.content

            let messageDate = Date.init(timeIntervalSince1970: TimeInterval(self.arrList[indexPath.row].lastMessage.timestamp/1000))
            let dataformatter = DateFormatter.init()
            dataformatter.timeStyle = .short
            let date = dataformatter.string(from: messageDate)
            cell.lblTime.text = date
            if self.arrList[indexPath.row].lastMessage.owner == "sender" && self.arrList[indexPath.row].lastMessage.isRead == false {
//                cell.nameLabel.font = UIFont(name:"AvenirNext-DemiBold", size: 17.0)
//                cell.messageLabel.font = UIFont(name:"AvenirNext-DemiBold", size: 14.0)
//                cell.timeLabel.font = UIFont(name:"AvenirNext-DemiBold", size: 13.0)
//                cell.profilePic.layer.borderColor = GlobalVariables.blue.cgColor
//                cell.messageLabel.textColor = GlobalVariables.purple
            }
            return cell
        }
        else
        {
            return UITableViewCell()
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let chatVC = APP_DELEGATE.stryBoard!.instantiateViewController(withIdentifier: "ChatViewController") as! ChatViewController
        chatVC.objUser = selectedIndex == 1 ? arrUserList[indexPath.row] : arrList[indexPath.row].user
//        let Loc = objPro.location
//        let fid = objPro.fid
//        let exceptID = Auth.auth().currentUser?.uid
        chatVC.onClickBack = {() -> Void in
//            if self.selectedIndex == 1
//            {
//                self.arrUserList.remove(at: indexPath.row)
//            }
//            else{
//                self.arrList.remove(at: indexPath.row)
//            }
//            self.conversationList(Loc as NSString, fid as NSString, exceptID!)
            self.btnUser.titleLabel?.textColor = .darkGray
            self.btnChatList.titleLabel?.textColor = .black
            self.lblLine1.isHidden = true
            self.lblLine2.isHidden = false
            self.selectedIndex = 2
            self.scrollViewMain.contentOffset.x = CGFloat(self.scrollViewMain.frame.size.width)
        }
        self.present(chatVC, animated: true, completion: nil)
    }
    
//    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
//        self.prototypeCell = tblViewList.dequeueReusableCell(withIdentifier: "myTrips_Cell")! as! MyTripsTableViewCell
//        let objVal:TripsDetailsBO = selectedIndex == 1 ? LOGIN_CONSTANT.objProfile.arrTrips[indexPath.row] as! TripsDetailsBO : LOGIN_CONSTANT.objProfile.arrParticipation[indexPath.row] as! TripsDetailsBO
//        let cellHgt = APP_DELEGATE.heightWithConstrainedWidth(width: prototypeCell.lblDesc.frame.size.width, font: prototypeCell.lblDesc.font, string: objVal.trip_desc)
//        objVal.cellHgt = cellHgt + 25
//        return 237 + cellHgt
//    }
    
    
    // MARK: -
    // MARK: - UIScrollViewDelegates
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        if scrollView == scrollViewMain {
            DispatchQueue.main.async {
                UIView.animate(withDuration: 0.2, delay: 0, options: UIViewAnimationOptions.curveEaseOut, animations: {
                 //   self.scrollViewMain.contentOffset.x = CGFloat(self.scrollViewMain.frame.size.width)
                    let pageNumber = round(scrollView.contentOffset.x / scrollView.frame.size.width)
                    if pageNumber == 0 {
                        self.btnUser.titleLabel?.textColor = .black
                        self.btnChatList.titleLabel?.textColor = .darkGray
                        self.lblLine1.isHidden = false
                        self.lblLine2.isHidden = true
                        self.selectedIndex = 1
                        self.scrollViewMain.contentOffset.x = CGFloat(0)
                        self.tbListUser.reloadData()
                        self.tbListUser.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
                    }
                    else{
                        self.btnUser.titleLabel?.textColor = .darkGray
                        self.btnChatList.titleLabel?.textColor = .black
                        self.lblLine1.isHidden = true
                        self.lblLine2.isHidden = false
                        self.selectedIndex = 2
                        self.scrollViewMain.contentOffset.x = CGFloat(self.scrollViewMain.frame.size.width)
                        self.tblChatList.reloadData()
                        self.tblChatList.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
                    }
                }, completion: nil)
            }
        }
        else {
            
            //something else
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
