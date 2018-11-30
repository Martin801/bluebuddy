//
//  MyListingServiceTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/21/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class MyListingServiceTableViewCell: UITableViewCell {

    @IBOutlet weak var imgFeatured: UIImageView!
    @IBOutlet weak var btnDetails: UIButton!
    @IBOutlet weak var btnEdit: UIButton!
    @IBOutlet weak var lblPostedBy: UILabel!
    @IBOutlet weak var lblAddress: UILabel!
    @IBOutlet weak var lblPrice: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgBG: UIImageView!
    @IBOutlet weak var imgViewPic: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
