package org.tang.exam.entity;

import org.tang.exam.R;
import org.tang.exam.common.AppConstant;

import android.text.TextUtils;


public class UserHelper {

	public static int getDefaultImage(UserInfo userInfo) {
		if (userInfo.getSex() == AppConstant.Sex.FEMALE) {
			return R.drawable.avatar_default_normal;
		} else {
			return R.drawable.avatar_default_normal;
		}
	}

	public static int getErrorImage(UserInfo userInfo) {
		if (userInfo.getSex() == AppConstant.Sex.FEMALE) {
			return R.drawable.avatar_default_normal;
		} else {
			return R.drawable.avatar_default_normal;
		}
	}

	public static String getRemark(UserInfo userInfo) {
		String remark = "";
		if (userInfo instanceof StudentRoster) {
			remark = "学生";
		} else if (userInfo instanceof ParentRoster) {
			ParentRoster parent = (ParentRoster) userInfo;
			remark = parent.getChildName() + "家长";
		} else if (userInfo instanceof TeacherRoster) {
			TeacherRoster teacher = (TeacherRoster) userInfo;
			remark = RolePaser.jsonToString(teacher.getRole()).trim();
			if (TextUtils.isEmpty(remark) || remark.equals("0")) {
				remark = "老师";
			}
		}

		return remark;
	}
}