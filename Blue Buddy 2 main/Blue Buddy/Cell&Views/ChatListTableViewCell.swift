//
//  ChatListTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/8/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class ChatListTableViewCell: UITableViewCell {

    @IBOutlet weak var imgCheck: UIImageView!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var lblInitial: UILabel!
    @IBOutlet weak var imgBg: UIImageView!
    @IBOutlet weak var imgUserPic: UIImageView!
    @IBOutlet weak var btnRemove: UIButton!
    
    var onClickBtnDelete: (() -> Void)? = nil
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        imgUserPic.layer.cornerRadius = imgUserPic.frame.size.height/2;
        imgUserPic.clipsToBounds = true
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @IBAction func onClickBtnDelete(_ sender: UIButton) {
        if ((onClickBtnDelete) != nil) {
            onClickBtnDelete!()
        }
    }

}
