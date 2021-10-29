package moe.ore.msg.helper

object TroopInfoUtil {

    /**
     * 检查群里面哪些功能是否开启 通过flag
     */

    fun isTroopWriteTogether(flagex3: Int) = flagex3.toLong() and 1073741824 == 0L // 群一起写

    fun isTroopLuckyChar(flagex4: Int) = (flagex4.toLong() and 32768) == 0L // 群幸运字

    fun isQidianPrivateTroop(flagex3: Int) = flagex3.toLong() and 32 != 0L // 未知的玩意

    fun isVisitorSpeakEnabled(flagex: Int) = flagex and 8192 == 8192

    fun isTroopIlive(flagex4: Int): Boolean {
        val z = flagex4 and 1024 != 0
        // QLog.i("IliveGroup", 1, String.format("isTroopIlive %s", z))
        return z
    }

    fun isAllowCreateTempConv(troopPrivilegeFlag: Int): Boolean {
        return troopPrivilegeFlag and 65536 == 0
    }

    fun isAllowCreateDiscuss(troopPrivilegeFlag: Int): Boolean {
        return troopPrivilegeFlag and 32768 == 0
    }

    fun isOnlyTroopMemberInviteOption(flagex3: Int): Boolean {
        return flagex3 and 4 != 0
    }

    fun isHomeworkTroop(dwGroupClassExt: Int): Boolean {
        return dwGroupClassExt == 32
    }

    fun isFansTroop(dwGroupClassExt: Int): Boolean {
        return dwGroupClassExt == 27
    }

    fun isGameTroop(dwGroupClassExt: Int): Boolean {
        return dwGroupClassExt == 25
    }

    fun isGameBind(dwGroupFlagExt3: Int): Boolean {
        return dwGroupFlagExt3 and 2048 != 0
    }

    fun isAutoApprovalOpen(dwGroupFlagExt3: Int): Boolean {
        return dwGroupFlagExt3 and 1048576 == 1048576
    }
    /*
    fun setAutoApprovalOpen(z: Boolean) {
        if (z) {
            this.dwGroupFlagExt3 = this.dwGroupFlagExt3 or 1048576
        } else {
            this.dwGroupFlagExt3 = this.dwGroupFlagExt3 and -1048577
        }
        if (QLog.isColorLevel()) {
            QLog.i(
                com.tencent.mobileqq.data.troop.TroopInfo.TAG,
                2,
                String.format(
                    "setAutoApprovalOpen [%s, %s]",
                    java.lang.Long.valueOf(this.dwGroupFlagExt3),
                    java.lang.Boolean.valueOf(z)
                )
            )
        }
    }
   */

    /* 是否有群头像
public static boolean hasCover(TroopInfo troopInfo) {
        if (troopInfo == null) {
            return false;
        }
        for (TroopClipPic troopClipPic : troopInfo.mTroopPicList) {
            if (troopClipPic.type == 0) {
                return true;
            }
        }
        return false;
    }
 */

    /*
    是否是群成员头像组成的群头像
    fun isUseClassAvatar(): Boolean {
        return !this.isNewTroop && this.dwGroupFlagExt and 131072 != 0L
    }

    fun setUseClassAvatar(z: Boolean) {
        if (z) {
            this.dwGroupFlagExt = this.dwGroupFlagExt or 131072
        } else {
            this.dwGroupFlagExt = this.dwGroupFlagExt and -131073
        }
    }
     */

    /*
    fun isThirdAppBind(): Boolean {
        return this.hlGuildBinary != 1
    }*/

    fun isListenTogetherOpen(dwGroupFlagExt3: Int): Boolean {
        return dwGroupFlagExt3 and 2097152 != 0
    }
/*
    fun setIsListenTogether(z: Boolean) {
        if (z) {
            this.dwGroupFlagExt3 = this.dwGroupFlagExt3 or 2097152
        } else {
            this.dwGroupFlagExt3 = this.dwGroupFlagExt3 and -2097153
        }
    }*/

    fun isWatchTogetherOpen(dwGroupFlagExt3: Int): Boolean {
        return dwGroupFlagExt3 and 4194304 != 0
    }/*
    fun setIsWatchTogether(z: Boolean) {
        if (z) {
            this.dwGroupFlagExt3 = this.dwGroupFlagExt3 or 4194304
        } else {
            this.dwGroupFlagExt3 = this.dwGroupFlagExt3 and -4194305
        }
    }*/

    fun isAVGameOpen(dwGroupFlagExt3: Int): Boolean {
        return dwGroupFlagExt3 and 268435456 != 0
    }/*
    fun setIsAVGameOpen(z: Boolean) {
        if (z) {
            this.dwGroupFlagExt3 = this.dwGroupFlagExt3 or VasBusiness.QWALLET
        } else {
            this.dwGroupFlagExt3 = this.dwGroupFlagExt3 and -268435457
        }
    }*/

    fun isOnlyAllowManagerCreateAVGame(dwGroupFlagExt3: Int): Boolean {
        return dwGroupFlagExt3 and 536870912 != 0
    }

    fun getStudyRoomOpen(groupFlagExt4: Int): Boolean {
        return checkFlagExt4(groupFlagExt4, 256)
    }

    private fun checkFlagExt4(groupFlagExt4: Int, dv: Int): Boolean {
        return dv and groupFlagExt4 > 0
    }
    /*
        public void setStudyRoomOpen(boolean z) {
        if (z) {
            this.groupFlagExt4 |= 256;
        } else {
            this.groupFlagExt4 &= -257;
        }
        QLog.i("StudyRoom", 2, String.format("setStudyRoomOpen %s %s", Boolean.valueOf(z), Boolean.valueOf(getStudyRoomOpen())));
    }
     */

    fun isSharingLocation(dwGroupFlagExt3: Int): Boolean {
        return dwGroupFlagExt3 and 67108864 != 0
    }/*
    fun setIsSharingLocation(z: Boolean) {
        if (z) {
            this.dwGroupFlagExt3 = this.dwGroupFlagExt3 or 67108864
        } else {
            this.dwGroupFlagExt3 = this.dwGroupFlagExt3 and -67108865
        }
    }*/

    @JvmOverloads
    fun isTogetherBusinessOpen(dwGroupFlagExt3: Int, i: Int = 16777216): Boolean {
        return dwGroupFlagExt3 and i != 0
    }

    fun isTroopHonorOpen(dwGroupFlagExt3: Int): Boolean {
        return dwGroupFlagExt3 and 33554432 == 0
    }

    /*
    fun isHistoryMsgReadEnableForNewMember(): Boolean {
        return this.isAllowHistoryMsgFlag == 1
    }*/

    fun isNotSetTroopClassInfo(groupFlagExt4: Int): Boolean {
        return groupFlagExt4 and 128 == 0
    }

    fun isTroopGameCardEnabled(groupFlagExt4: Int): Boolean {
        return groupFlagExt4 and 4096 != 0
    }

    /*
        public boolean isShowMyGameCardEnabled() {
        return (this.cmduinFlagEx3Grocery & 1) == 0;
    }

    fun isNeedClearAutoApproval(): Boolean {
        return this.cGroupOption.toInt() != 2 || this.troopPrivilegeFlag and 512 == 512L
    }

    fun isKingBattleTroop(): Boolean {
        if (QLog.isColorLevel()) {
            QLog.d(
                com.tencent.mobileqq.data.troop.TroopInfo.TAG,
                2,
                "hlGuildAppid:" + this.hlGuildAppid + ",subType:" + this.hlGuildSubType
            )
        }
        return this.hlGuildAppid == 1104466820L && this.hlGuildSubType == 0L
    }*/

    /*
        public boolean isExited() {
        return this.exitTroopReason != 0;
    }

    public boolean isKicked() {
        return this.exitTroopReason == 1;
    }

    public boolean isDisband() {
        return this.exitTroopReason == 2;
    }
     */
}