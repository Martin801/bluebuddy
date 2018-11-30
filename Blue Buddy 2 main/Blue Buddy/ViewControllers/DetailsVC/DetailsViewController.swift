//
//  DetailsViewController.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/12/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class DetailsViewController: UIViewController, UIScrollViewDelegate, UITableViewDelegate, UITableViewDataSource{

    @IBOutlet weak var lblLine2: UILabel!
    @IBOutlet weak var lblLine1: UILabel!
    @IBOutlet weak var tblViewList: UITableView!
    @IBOutlet weak var txtView: UITextView!
    @IBOutlet weak var scrollViewBtm: UIScrollView!
    @IBOutlet weak var viewSelection: UIView!
    @IBOutlet weak var viewHeader: UIView!
    @IBOutlet weak var scrollViewMain: UIScrollView!
    @IBOutlet weak var viewTop: UIView!
    override func viewDidLoad() {
        super.viewDidLoad()
        scrollViewMain.frame = CGRect(x: CGFloat(0), y: CGFloat(65), width: CGFloat(view.frame.size.width), height: CGFloat(view.frame.size.height - 65))
        viewSelection.frame = CGRect(x: CGFloat(0), y: CGFloat(viewHeader.frame.maxY), width: CGFloat(view.frame.size.width), height: CGFloat(45))
        scrollViewBtm.frame = CGRect(x: CGFloat(0), y: CGFloat(viewSelection.frame.maxY), width: CGFloat(view.frame.size.width * 2), height: CGFloat(view.frame.size.height - viewSelection.frame.maxY))
        txtView.frame = CGRect(x: CGFloat(20), y: CGFloat(20), width: CGFloat(view.frame.size.width - 40), height: CGFloat(scrollViewBtm.frame.size.height))
        tblViewList.frame = CGRect(x: CGFloat(view.frame.size.width + 20), y: CGFloat(20), width: CGFloat(view.frame.size.width - 40), height: CGFloat(scrollViewBtm.frame.size.height))
        scrollViewBtm.contentSize = CGSize (width:view.frame.size.width * 2, height:txtView.frame.maxY)
        scrollViewMain.contentSize = CGSize (width:scrollViewMain.frame.size.width, height: scrollViewBtm.frame.maxY + viewHeader.frame.maxY + 65)
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func onClickBtnSelectionHeader(_ sender: UIButton) {
        
        if  sender.tag == 1 {
            lblLine2.isHidden = true
            lblLine1.isHidden = false
//            btnParticipants.titleLabel?.textColor = .darkGray
//            lblLine3.isHidden = false
//            lblLine4.isHidden = true
//            btnParticipants1.titleLabel?.textColor = .darkGray
//            viewTblHgtConstraint.constant = txtDetails.frame.size.height
//            contentViewHgtConstraint.constant = selectionView2.frame.maxY + txtDetails.frame.size.height
//            selectedIndex = 1
            scrollViewBtm.frame = CGRect(x: CGFloat(0), y: CGFloat(viewSelection.frame.maxY), width: CGFloat(view.frame.size.width * 2), height: CGFloat(view.frame.size.height - viewSelection.frame.maxY))
        }
        else{
            lblLine1.isHidden = true
            lblLine2.isHidden = false
//            btnDetails1.titleLabel?.textColor = .darkGray
//            lblLine3.isHidden = true
//            lblLine4.isHidden = false
//            btnDetails.titleLabel?.textColor = .darkGray
//            viewTblHgtConstraint.constant = CGFloat(arrParticipants.count * 95)
//            contentViewHgtConstraint.constant = selectionView2.frame.maxY + (5 * 95)
//            selectedIndex = 2
            tblViewList.frame = CGRect(x: CGFloat(view.frame.size.width + 20), y: CGFloat(20), width: CGFloat(view.frame.size.width - 40), height: CGFloat(15 * 75))
            scrollViewBtm.frame = CGRect(x: CGFloat(0), y: CGFloat(viewSelection.frame.maxY), width: CGFloat(view.frame.size.width * 2), height: CGFloat(1075))
        }
        let pageWidth:CGFloat = view.frame.width
        let contentOffset:CGFloat = scrollViewBtm.contentOffset.x
        
        var slideToX = contentOffset + pageWidth
        
        if  sender.tag == 1
        {
            slideToX = 0
        }
        else{
            slideToX = contentOffset + pageWidth
        }
        scrollViewBtm.setContentOffset(CGPoint(x: slideToX, y: 0), animated: true)
        
    }
    
    // MARK: -
    // MARK: - UITableViewDelegates & Datasources
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return 15
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        let identifier = "part_list"
        var cell: ParticipantListTableViewCell! = tableView.dequeueReusableCell(withIdentifier: identifier) as? ParticipantListTableViewCell
        if cell == nil {
            tableView.register(UINib(nibName: "ParticipantListTableViewCell", bundle: nil), forCellReuseIdentifier: identifier)
            cell = tableView.dequeueReusableCell(withIdentifier: identifier) as? ParticipantListTableViewCell
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 75
    }
    
    
    // MARK: -
    // MARK: - ScrollView Delegates
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollViewMain.contentOffset.y > viewHeader.frame.size.height
        {
//            selectionView1.isHidden = false
//            self.view.bringSubview(toFront: selectionView1)
//            lblHeaderTitle.text = objTripDetail.activity
        }
        else{
//            selectionView1.isHidden = true
//            lblHeaderTitle.text = "TRIP DETAILS"
        }
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView){
        // Test the offset and calculate the current page after scrolling ends
         if scrollView == scrollViewBtm
        {
            if scrollView == tblViewList
            {
                
            }
            else 
            {
            let pageWidth:CGFloat = scrollView.frame.width
            let currentPage:CGFloat = floor((scrollView.contentOffset.x-pageWidth/2)/pageWidth)+1
            if  Int(currentPage) == 0 {
                lblLine2.isHidden = true
                lblLine1.isHidden = false
//                lblLine3.isHidden = false
//                lblLine4.isHidden = true
//                btnParticipants.titleLabel?.textColor = .darkGray
//                btnParticipants1.titleLabel?.textColor = .darkGray
//                viewTblHgtConstraint.constant = txtDetails.frame.size.height
//
//                selectedIndex = 1
                scrollViewBtm.frame = CGRect(x: CGFloat(0), y: CGFloat(viewSelection.frame.maxY), width: CGFloat(view.frame.size.width * 2), height: CGFloat(txtView.frame.maxY))
            }
            else{
                lblLine1.isHidden = true
                lblLine2.isHidden = false
//                btnDetails1.titleLabel?.textColor = .darkGray
//                lblLine3.isHidden = true
//                lblLine4.isHidden = false
//                btnDetails.titleLabel?.textColor = .darkGray
//                viewTblHgtConstraint.constant = CGFloat(arrParticipants.count * 95)
//                selectedIndex = 2
                scrollViewBtm.frame = CGRect(x: CGFloat(0), y: CGFloat(viewSelection.frame.maxY), width: CGFloat(view.frame.size.width * 2), height: CGFloat(1075))
            }
            }
        }
        else if (scrollView == scrollViewMain)
        {
//            contentViewHgtConstraint.constant = selectedIndex == 1 ? selectionView2.frame.maxY + txtDetails.frame.size.height : selectionView2.frame.maxY + (5 * 95)
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
