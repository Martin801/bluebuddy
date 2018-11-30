//
//  ChatViewController.swift
//  Blue Buddy
//
//  Created by AQUARIOUS on 25/01/18.
//  Copyright © 2018 Aquatech iOS. All rights reserved.
//

import UIKit
import Firebase
class ChatViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate {

    @IBOutlet weak var btnMsg: UIButton!
    @IBOutlet weak var txtMsg: UITextField!
    @IBOutlet var viewInputBar: UIView!
    @IBOutlet weak var tblMsgList: UITableView!
    @IBOutlet weak var lblTitle: UILabel!
    var dataformatter = DateFormatter()
    var objUser: User?
    let barHeight: CGFloat = 50
    var arrMsgList = [Message]()
    var onClickBack: (() -> Void)? = nil
    override var inputAccessoryView: UIView? {
        get {
            self.viewInputBar.frame.size.height = self.barHeight
            self.viewInputBar.clipsToBounds = true
            return self.viewInputBar
        }
    }
    override var canBecomeFirstResponder: Bool{
        return true
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tblMsgList.estimatedRowHeight = self.barHeight
        self.tblMsgList.rowHeight = UITableViewAutomaticDimension
        self.tblMsgList.contentInset.bottom = self.barHeight
        
        self.tblMsgList.scrollIndicatorInsets.bottom = self.barHeight
        let myColor = UIColor.init(red: 0/255.0, green: 56/255.0, blue: 118/255.0, alpha: 1.0)
        btnMsg.layer.borderColor         = myColor.cgColor
        btnMsg.layer.borderWidth         = 2.0
        self.txtMsg.layer.cornerRadius = self.txtMsg.frame.size.height/2
        self.btnMsg.layer.cornerRadius = self.btnMsg.frame.size.height/2
        self.btnMsg.clipsToBounds = true
        self.fetchData()
        dataformatter = DateFormatter.init()
        dataformatter.timeStyle = .short
        lblTitle.text = objUser?.name.uppercased()

        // Do any additional setup after loading the view.
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        self.viewInputBar.backgroundColor = UIColor.clear
        self.view.layoutIfNeeded()
        NotificationCenter.default.addObserver(self, selector: #selector(ChatViewController.showKeyboard(notification:)), name: Notification.Name.UIKeyboardWillShow, object: nil)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        NotificationCenter.default.removeObserver(self)
        Message.markMessagesRead(forUserID: self.objUser!.id)
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    //MARK:- UIButton Action
    @IBAction func onClickBtnBack(_ sender: UIButton) {
        if onClickBack != nil {
            onClickBack!()
        }
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func onClickBtnSend(_ sender: UIButton) {
        
        let dictVal = NSMutableDictionary()
        dictVal["message"] = self.txtMsg.text
        dictVal["email"] = objUser?.email
        
        if let text = self.txtMsg.text {
            if text.characters.count > 0 {
                self.txtMsg.text = ""
                self.composeMessage(content: text)
            }
        }
       
//        dictVal["device_type"] = "ios"
        DispatchQueue.global().async {
            TripsParser.callChatService(dictVal) { (status, strCounter) in
                
            }
        }
    }
    
    //MARK:- Others Account
    func fetchData() {
        Message.downloadAllMessages(forUserID: self.objUser!.id, completion: {[weak weakSelf = self] (message) in
            weakSelf?.arrMsgList.append(message)
          //  weakSelf?.arrMsgList.sort{ $0.timestamp < $1.timestamp }
            DispatchQueue.main.async {
                if let state = weakSelf?.arrMsgList.isEmpty, state == false {
                    weakSelf?.tblMsgList.reloadData()
                    weakSelf?.tblMsgList.scrollToRow(at: IndexPath.init(row: self.arrMsgList.count - 1, section: 0), at: .bottom, animated: false)
                }
            }
        })
        Message.markMessagesRead(forUserID: self.objUser!.id)
    }
    func composeMessage(content: String)  {
        
        let message = Message.init(content: content, owner: "sender", timestamp: 0, isRead: false)
        Message.send(message: message, toID: self.objUser!.id, completion: {(_) in
        })
    }
    
    //MARK: NotificationCenter handlers
    @objc func showKeyboard(notification: Notification) {
        if let frame = notification.userInfo![UIKeyboardFrameEndUserInfoKey] as? NSValue {
            let height = frame.cgRectValue.height
            self.tblMsgList.contentInset.bottom = height
            self.tblMsgList.scrollIndicatorInsets.bottom = height
            if self.arrMsgList.count > 0 {
                self.tblMsgList.scrollToRow(at: IndexPath.init(row: self.arrMsgList.count - 1, section: 0), at: .bottom, animated: true)
            }
        }
    }
    
    //MARK:  TableView Delegates & Datasources
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.arrMsgList.count
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if tableView.isDragging {
            cell.transform = CGAffineTransform.init(scaleX: 0.5, y: 0.5)
            UIView.animate(withDuration: 0.3, animations: {
                cell.transform = CGAffineTransform.identity
            })
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        

        switch self.arrMsgList[indexPath.row].owner {
           
        case "receiver":
            let cell = tableView.dequeueReusableCell(withIdentifier: "Receiver", for: indexPath) as! ReceiverCell
            cell.clearCellData()
            cell.message.text = self.arrMsgList[indexPath.row].content
            let messageDate = Date.init(timeIntervalSince1970: TimeInterval(self.arrMsgList[indexPath.row].timestamp/1000))
            let date = dataformatter.string(from: messageDate)
            cell.lblChatTime.text = date

            return cell
        case "sender":
            let cell = tableView.dequeueReusableCell(withIdentifier: "Sender", for: indexPath) as! SenderCell
            cell.clearCellData()
            let messageDate = Date.init(timeIntervalSince1970: TimeInterval(self.arrMsgList[indexPath.row].timestamp/1000))
            let date = dataformatter.string(from: messageDate)
            cell.profilePic.layer.borderWidth = 0.5
            cell.profilePic.layer.borderColor = UIColor.lightGray.cgColor
            cell.profilePic.layer.cornerRadius = cell.profilePic.frame.size.height/2
            cell.profilePic.kf.setImage(with: URL(string:(self.objUser?.profilePic)!), placeholder: #imageLiteral(resourceName: "user.png"), options: [.transition(.fade(1))], progressBlock: nil,completionHandler: nil)
         //   cell.profilePic.image = self.fr?.profilePic
            cell.message.text = self.arrMsgList[indexPath.row].content
            cell.lblTime.text = date

            return cell
        default:
            break
        }
        return UITableViewCell()
    }
    //MARK:  TextFeild Delegates
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
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
