//
//  CertificateListTableViewCell.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 11/20/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class CertificateListTableViewCell: UITableViewCell{

    @IBOutlet weak var lbl3HgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblLevel2HgtConstraint: NSLayoutConstraint!
    @IBOutlet weak var lblLevel3: UILabel!
    @IBOutlet weak var lblLevel2: UILabel!
    @IBOutlet weak var lblLevel1: UILabel!

    @IBOutlet weak var lblCertNO: UILabel!
    @IBOutlet weak var lblAgencyName: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgBg: UIImageView!
    
    var onClickEdit: (() -> Void)? = nil
    var onClickDelete: (() -> Void)? = nil
    override func awakeFromNib() {
        super.awakeFromNib()
        
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    // MARK: -
    // MARK: - UIButton Actions
    @IBAction func onClickBtnDelete(_ sender: UIButton) {
        if ((onClickDelete) != nil) {
            onClickDelete!()
        }
    }
    @IBAction func onClickBtnEdit(_ sender: UIButton) {
        if ((onClickEdit) != nil) {
            onClickEdit!()
        }
    }
    
}
