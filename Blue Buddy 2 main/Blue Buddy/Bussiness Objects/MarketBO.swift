//
//  MarketBO.swift
//  Blue Buddy
//
//  Created by Aquatech iOS on 12/12/17.
//  Copyright © 2017 Aquatech iOS. All rights reserved.
//

import UIKit

class MarketBO: NSObject {

}

class CharterDetailsBO: NSObject {
    var name: String            = ""
    var user_pic: String            = ""
    var user_email: String          = ""
    var user_Phone: String          = ""
    var user_id: String             = ""
    var user_name: String           = ""
    var price: String           = ""
    var charter_id: String            = ""
    var capacity: String            = ""
    var charter_desc: String            = ""
    var half_price: String            = ""
    var is_editable: Bool           = false
    var is_reviewed: Bool           = false
    var user_loc: String           = ""
    var is_flagged: Bool            = false
    var is_featured: Bool            = false
    var is_hidden :Bool              = false
    var firebase_id: String = ""
    var rating :Int              = 0
    var size :String              = ""
    var boat_type :String              = ""
    var charter_image :String               = ""
    var arrReviews :NSMutableArray               = []
}
class CoursesDetailsBO: NSObject {
    var name: String            = ""
    var user_pic: String            = ""
    var user_email: String          = ""
    var user_Phone: String          = ""
    var user_id: String             = ""
    var user_name: String           = ""
    var price: String           = ""
    var course_id: String            = ""
    var category: String            = ""
    var is_editable: Bool           = false
    var is_reviewed: Bool           = false
    var user_loc: String           = ""
    var agency_name: String           = ""
    var agency_url: String           = ""
    var course_desc: String           = ""
    var duration: String           = ""
    var is_flagged: Bool            = false
    var is_featured: Bool            = false
    var firebase_id: String = ""
    var rating :Int              = 0
    var course_image :String               = ""
    var arrReviews :NSMutableArray               = []
}


class ReviewBO: NSObject
{
    var itemName:String  = ""
    var item_id:String  = ""
    var review_title: String            = ""
    var user_pic: String            = ""
    var rating: String          = ""
    var review_id: String          = ""
    var user_id: String             = ""
    var user_name: String           = ""
    var date: String           = ""
    var for_id: String           = ""
    var review_for:String          = ""
    var reviewDesc: String           = ""
    var cellHgt: CGFloat = 0.0

}

class ProductDetailsBO: NSObject {
    var name: String            = ""
    var product_type: String            = ""
    var user_pic: String            = ""
    var user_email: String          = ""
    var user_Phone: String          = ""
    var user_id: String             = ""
    var user_name: String           = ""
    var price: String           = ""
    var product_id: String            = ""
    var category: String            = ""
    var is_editable: Bool           = false
    var is_reviewed: Bool           = false
    var user_loc: String           = ""
    var product_desc: String           = ""
    var duration: String           = ""
    var is_flagged: Bool            = false
    var is_featured: Bool            = false
    var is_hidden :Bool              = false
    var firebase_id: String = ""
    var rating :Int              = 0
    var arrReviews :NSMutableArray               = []
    var arrImages :NSMutableArray               = []
}
class ImageBO: NSObject {
    var image_name: String            = ""
    var image_id: String            = ""
    var locImg: UIImage? = nil
}

class ServiceDetailsBO: NSObject {
    var name: String            = ""
    var user_pic: String            = ""
    var user_email: String          = ""
    var user_Phone: String          = ""
    var user_id: String             = ""
    var user_name: String           = ""
    var price: String           = ""
    var service_id: String            = ""
    var service_type: String            = ""
    var category: String            = ""
    var is_editable: Bool           = false
    var is_reviewed: Bool           = false
    var user_loc: String           = ""
    var user_lat: String           = ""
    var user_long: String           = ""
    var service_desc: String           = ""
    var duration: String           = ""
    var is_flagged: Bool            = false
    var is_featured: Bool            = false
    var rating :String              = ""
    var is_hidden :Bool              = false
    var firebase_id: String = ""
    var service_image :String               = ""
    var arrReviews :NSMutableArray               = []
}


