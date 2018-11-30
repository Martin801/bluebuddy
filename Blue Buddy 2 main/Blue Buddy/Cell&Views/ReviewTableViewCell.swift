//
//  ReviewTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/1/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit
import FloatRatingView
class ReviewTableViewCell: UITableViewCell {

    @IBOutlet weak var imgBg: UIImageView!
    @IBOutlet weak var lblDescHgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var imgUser: UIImageView!
    @IBOutlet weak var lblreview: UILabel!
    @IBOutlet weak var viewRating: FloatRatingView!
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var btnProfile: UIButton!

    var onClickProfile: (() -> Void)? = nil
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    @IBAction func onClickBtnProfile(_ sender: UIButton) {
        if ((onClickProfile) != nil)
        {
            onClickProfile!()
        }
    }
}
