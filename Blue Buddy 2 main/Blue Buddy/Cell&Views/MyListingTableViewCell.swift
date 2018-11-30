//
//  MyListingTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/21/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class MyListingTableViewCell: UITableViewCell {

    @IBOutlet weak var imgFeatured: UIImageView!
    @IBOutlet weak var lblNo: UILabel!
    @IBOutlet weak var lblAddress: UILabel!
    @IBOutlet weak var lblPostedBy: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgBg: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
