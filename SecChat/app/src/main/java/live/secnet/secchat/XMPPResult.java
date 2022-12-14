package live.secnet.secchat;

public interface XMPPResult {
    void onResultCallback(XMPPThread.RESULT rst, XMPPThread.REQUEST_TYPE req);
}
