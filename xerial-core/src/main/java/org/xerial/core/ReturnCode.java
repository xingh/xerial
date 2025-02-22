/*--------------------------------------------------------------------------
 *  Copyright 2010 Taro L. Saito
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// XerialJ
//
// ReturnCode.java
// Since: Sep 22, 2010 11:35:27 AM
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.core;

/**
 * Return codes of command-line programs
 * 
 * @author leo
 * 
 */
public enum ReturnCode {

    FAILURE(-1) /* error occurred */, SUCCESS(0) /* normal termination */,
    EPERM(1) /* Operation not permitted */, ENOENT(2) /* No such file or directory */,
    ESRCH(3) /* No such process */, EINTR(4) /* Interrupted system call */,
    EIO(5) /* Input/output error */, ENXIO(6) /* Device not configured */,
    E2BIG(7) /* Argument list too long */, ENOEXEC(8) /* Exec format error */,
    EBADF(9) /* Bad file descriptor */, ECHILD(10) /* No child processes */,
    EDEADLK(11) /* Resource deadlock avoided */, ENOMEM(12) /* Cannot allocate memory */, EACCES(
            13) /* Permission denied */, EFAULT(14) /* Bad address */,
    ENOTBLK(15) /* Block device required */, EBUSY(16) /* Device / Resource busy */,
    EEXIST(17) /* File exists */, EXDEV(18) /* Cross-device link */,
    ENODEV(19) /* Operation not supported by device */, ENOTDIR(20) /* Not a directory */,
    EISDIR(21) /* Is a directory */, EINVAL(22) /* Invalid argument */,
    ENFILE(23) /* Too many open files in system */, EMFILE(24) /* Too many open files */, ENOTTY(
            25) /* Inappropriate ioctl for device */, ETXTBSY(26) /* Text file busy */,
    EFBIG(27) /* File too large */, ENOSPC(28) /* No space left on device */,
    ESPIPE(29) /* Illegal seek */, EROFS(30) /* Read-only file system */,
    EMLINK(31) /* Too many links */, EPIPE(32) /* Broken pipe */,
    EDOM(33) /* Numerical argument out of domain */, ERANGE(34) /* Result too large */,
    EAGAIN(35) /* Resource temporarily unavailable */,
    EWOULDBLOCK(35) /* Operation would block */, EINPROGRESS(36) /* Operation now in progress */,
    EALREADY(37) /* Operation already in progress */,
    ENOTSOCK(38) /* Socket operation on non-socket */,
    EDESTADDRREQ(39) /* Destination address required */, EMSGSIZE(40) /* Message too long */,
    EPROTOTYPE(41) /* Protocol wrong type for socket */,
    ENOPROTOOPT(42) /* Protocol not available */,
    EPROTONOSUPPORT(43) /* Protocol not supported */,
    ESOCKTNOSUPPORT(44) /* Socket type not supported */,
    ENOTSUP(45) /* Operation not supported */,
    EPFNOSUPPORT(46) /* Protocol family not supported */,
    EAFNOSUPPORT(47) /* Address family not supported by protocol family */,
    EADDRINUSE(48) /* Address already in use */,
    EADDRNOTAVAIL(49) /* Can't assign requested address */, ENETDOWN(50) /* Network is down */,
    ENETUNREACH(51) /* Network is unreachable */,
    ENETRESET(52) /* Network dropped connection on reset */,
    ECONNABORTED(53) /* Software caused connection abort */,
    ECONNRESET(54) /* Connection reset by peer */, ENOBUFS(55) /* No buffer space available */,
    EISCONN(56) /* Socket is already connected */, ENOTCONN(57) /* Socket is not connected */,
    ESHUTDOWN(58) /* Can't send after socket shutdown */,
    ETOOMANYREFS(59) /* Too many references: can't splice */,
    ETIMEDOUT(60) /* Operation timed out */, ECONNREFUSED(61) /* Connection refused */,
    ELOOP(62) /* Too many levels of symbolic links */, ENAMETOOLONG(63) /* File name too long */,
    EHOSTDOWN(64) /* Host is down */, EHOSTUNREACH(65) /* No route to host */,
    ENOTEMPTY(66) /* Directory not empty */, EPROCLIM(67) /* Too many processes */,
    EUSERS(68) /* Too many users */, EDQUOT(69) /* Disc quota exceeded */,
    ESTALE(70) /* Stale NFS file handle */, EREMOTE(71) /* Too many levels of remote in path */,
    EBADRPC(72) /* RPC struct is bad */, ERPCMISMATCH(73) /* RPC version wrong */, EPROGUNAVAIL(
            74) /* RPC prog. not avail */, EPROGMISMATCH(75) /* Program version wrong */,
    EPROCUNAVAIL(76) /* Bad procedure for program */, ENOLCK(77) /* No locks available */,
    ENOSYS(78) /* Function not implemented */,
    EFTYPE(79) /* Inappropriate file type or format */, EAUTH(80) /* Authentication error */,
    ENEEDAUTH(81) /* Need authenticator */, EPWROFF(82) /* Device power is off */,
    EDEVERR(83) /* Device error, e.g. paper out */,
    EOVERFLOW(84) /* Value too large to be stored in data type */,
    EBADEXEC(85) /* Bad executable */, EBADARCH(86) /* Bad CPU type in executable */, ESHLIBVERS(
            87) /* Shared library version mismatch */, EBADMACHO(88) /* Malformed Macho file */,
    ECANCELED(89) /* Operation canceled */, EIDRM(90) /* Identifier removed */,
    ENOMSG(91) /* No message of desired type */, EILSEQ(92) /* Illegal byte sequence */, ENOATTR(
            93) /* Attribute not found */, EBADMSG(94) /* Bad message */,
    EMULTIHOP(95) /* Reserved */, ENODATA(96) /* No message available on STREAM */,
    ENOLINK(97) /* Reserved */, ENOSR(98) /* No STREAM resources */,
    ENOSTR(99) /* Not a STREAM */, EPROTO(100) /* Protocol error */,
    ETIME(101) /* STREAM ioctl timeout */,
    EOPNOTSUPP(102) /* Operation not supported on socket */,
    ENOPOLICY(103) /* No such policy registered */, ELAST(103) /* Must be equal largest errno */;

    private final int code;

    ReturnCode(int code) {
        this.code = code;
    }

    public int toInt() {
        return code;
    }

}
