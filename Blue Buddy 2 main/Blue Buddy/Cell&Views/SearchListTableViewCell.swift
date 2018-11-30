//
//  SearchListTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/9/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class SearchListTableViewCell: UITableViewCell {

    @IBOutlet weak var lblAddBuddy: UILabel!
    @IBOutlet weak var btnAddBuddy: UIButton!
    @IBOutlet weak var imgProfile: UIImageView!
    @IBOutlet weak var imgOne: UIImageView!
    @IBOutlet weak var viewTwoImages: UIView!
    @IBOutlet weak var lblAddress: UILabel!
    @IBOutlet weak var lblNames: UILabel!
    @IBOutlet weak var imgBG: UIImageView!
    
    var onClickAddBuddy: (() -> Void)? = nil
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    @IBAction func onClickBtnAddBuddy(_ sender: UIButton) {
        
        if ((onClickAddBuddy) != nil) {
            onClickAddBuddy!()
        }
    }
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
