//
//  NewViewController.swift
//  Blue Buddy
//
//  Created by AQUARIOUS on 04/01/18.
//  Copyright © 2018 Aquatech iOS. All rights reserved.
//

import UIKit



class NewViewController: UIViewController, UITableViewDelegate, UITableViewDataSource , UIScrollViewDelegate{
    
    @IBOutlet weak var btnBuddy: UIButton!
    @IBOutlet weak var lblNoData: UILabel!
    @IBOutlet weak var img1: UIImageView!
    @IBOutlet weak var tblTrip: UITableView!
    @IBOutlet weak var lblNotifications: UILabel!
    @IBOutlet weak var lblBuddiesNo: UILabel!
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
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet var viewTop: UIView!


    override func viewDidLoad() {
        super.viewDidLoad()

        btnBuddy.isHidden = true
        img1.isHidden = true
        lblNoData.isHidden = true
    
        line2.isHidden = true
        
        btnTrips.titleLabel?.textColor = .darkGray
        line1.isHidden = false
        lblBuddiesNo.layer.cornerRadius = lblBuddiesNo.frame.size.height/2
        lblBuddiesNo.clipsToBounds = true
        lblNotifications.layer.cornerRadius = lblNotifications.frame.size.height/2
        lblNotifications.clipsToBounds = true

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    @IBAction func onClickBtnSelectionHeader(_ sender: UIButton) {
        if  sender.tag == 1 {
            line2.isHidden = true
            line1.isHidden = false
            btnTrips.titleLabel?.textColor = .darkGray
           
        }
        else{
            line1.isHidden = true
            line2.isHidden = false
            btnAbout.titleLabel?.textColor = .darkGray
           
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
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return 10
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
            let cell = tableView.dequeueReusableCell(withIdentifier: "myTrips_Cell", for: indexPath) as! MyTripsTableViewCell
            cell.imgBg.layer.borderWidth = 0.5
            cell.imgBg.layer.borderColor = UIColor.lightGray.cgColor
            cell.imgBg.layer.cornerRadius = 8.0
            cell.imgBg.layer.shadowColor = UIColor.lightGray.cgColor
            
            cell.lblNo.layer.cornerRadius = cell.lblNo.frame.size.width/2
            cell.lblNo.clipsToBounds = true
            cell.btnEdit.isHidden = true
            return cell
            
        
    }
    
    // MARK: -
    // MARK: - ScrollView Delegates
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollViewMain.contentOffset.y >= viewHeader.frame.size.height
        {
            selectionView.isHidden = true
            self.view.bringSubview(toFront: selectionView)
            lblTitle.text = "Steve Jobs"
            print(scrollViewMain.contentOffset.y)
            if (scrollViewMain.contentOffset.y >= viewHeader.frame.size.height) {
                scrollViewMain.isScrollEnabled = false
                scrollViewMain.setContentOffset(CGPoint(x: 0,y: viewHeader.frame.size.height), animated: true)
                scrollViewMain.isScrollEnabled = true
            }
            //self.kilScroll()
        }
        else{
            selectionView.isHidden = true
            lblTitle.text = "PROFILE"
        }
    }
    
    func kilScroll() {
       //isScrollKilled = true
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
                btnTrips.titleLabel?.textColor = .darkGray
            }
            else{
                line1.isHidden = true
                line2.isHidden = false
                btnAbout.titleLabel?.textColor = .darkGray
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
