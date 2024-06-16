package net.pro.mikiway.data.models

data class User(
    var userId: String = "",
    var userSlug : String = "",
    var userName : String = "",
    var userPassword : String = "",
    var userSalf : String = "",
    var userEmail: String = "",
    var userPhone : String = "",
    var userSex : String = "",
    var userAvatar: String = "",
    var userDataOfBirth: String = "",
    var userRole: Array<String> = arrayOf(),
    var address: String = ""
)

//usr_id: { type: Number, required: true }, // user
//usr_slug: { type: String, required: true },
//usr_name: { type: String, default: '' },
//usr_password: { type: String, default: '' },
//usr_salf: { type: String, default: '' },
//usr_email: { type: String, required: true },
//usr_phone: { type: String, default: '' },
//usr_sex: { type: String, default: '' },
//usr_avatar: { type: String, default: '' },
//usr_date_of_birth: { type: Date, default: null },
//usr_role: { type: Schema.Types.ObjectId, ref: 'Role' },
//usr_status: { type: String, default: 'pending', enum: [ 'pending', 'active', 'block' ] },
//address: { type: String, default: "" },