package metacourse.tsg

fun naiveStringMatcher(): ProgR {
    val eSubstr = Pve("substr")
    val eString = Pve("string")
    val eStringTail = Pve("string_tail")
    val eSubstring = Pve("substring")
    val eSubs = Pve("subs")
    val eStr = Pve("str")
    val eSubsHead = Pve("subs-head")
    val eSubsTail = Pve("subs_tail")
    val eStrHead = Pve("str-head")
    val eStrTail = Pve("str-tail")
    val aStrHead = Pva("str-head")
//    val a_str_tail = Pva("str-tail")
    val aSubsHead = Pva("subs_head")
    val e_ = Pve("_")
    val a_ = Pva("_")
    val FAILURE = Ret(Atom("FAILURE"))
    val SUCCESS = Ret(Atom("SUCCESS"))

    return listOf(
        DEFINE("Match", eSubstr, eString) {
            CALL("CheckPos", eSubstr, eString, eSubstr, eString)
        },
        DEFINE("CheckPos", eSubs, eStr, eSubstring, eString) {
            IF (MATCH(eSubs, eSubsHead, eSubsTail, a_)) {
                IF (MATCH(eSubsHead, e_,  e_,  aSubsHead)) {
                    FAILURE
                } ELSE {
                    IF (MATCH(eStr, eStrHead, eStrTail, a_)) {
                        IF (MATCH(eStrHead, e_, e_, aStrHead)) {
                            FAILURE
                        } ELSE {
                            IF (MATCH(eStrHead, e_, e_, aStrHead)) {
                                FAILURE
                            } ELSE {
                                IF (EQ(aSubsHead, aStrHead)) {
                                    CALL("CheckPos", eSubsTail, eStrTail, eSubstring, eString)
                                } ELSE {
                                    CALL("NextPos", eSubstring, eString)
                                }
                            }
                        }
                    } ELSE {
                        FAILURE
                    }
                }
            } ELSE {
                SUCCESS
            }
        },
        DEFINE("NextPos", eSubstring, eString) {
            IF (MATCH( eString, e_,  eStringTail, a_)) {
                CALL("Match", eSubstring, eStringTail)
            } ELSE {
                FAILURE
            }
        }
    )
}