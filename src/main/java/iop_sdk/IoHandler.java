package iop_sdk;

import java.io.IOException;

import iop_sdk.profile_server.IoSession;

/**
 * Created by Matias Furszyfer on 09/09/16.
 */
public interface IoHandler<M> {

    /**
     * Invoked from an I/O processor thread when a new connection has been created.
     * Because this method is supposed to be called from the same thread that
     * handles I/O of multiple sessions, please implement this method to perform
     * tasks that consumes minimal amount of time such as socket parameter
     * and user-defined session attribute initialization.
     *
     * @param session The session being created
     * @throws Exception If we get an exception while processing the create event
     */
    void sessionCreated(IoSession session) throws Exception;

    /**
     * Invoked when a connection has been opened.  This method is invoked after
     * {@link #sessionCreated(IoSession)}.  The biggest difference from
     * {@link #sessionCreated(IoSession)} is that it's invoked from other thread
     * than an I/O processor thread once thread model is configured properly.
     *
     * @param session The session being opened
     * @throws Exception If we get an exception while processing the open event
     */
    void sessionOpened(IoSession session) throws Exception;

    /**
     * Invoked when a connection is closed.
     *
     * @param session The session being closed
     * @throws Exception If we get an exception while processing the close event
     */
    void sessionClosed(IoSession session) throws Exception;

    /**
     * Invoked with the related {@link IdleStatus} when a connection becomes idle.
     * This method is not invoked if the transport type is UDP; it's a known bug,
     * and will be fixed in 2.0.
     *
     * @param session The idling session
     * @param status The session's status
     * @throws Exception If we get an exception while processing the idle event
     */
//    void sessionIdle(IoSession session, IdleStatus status) throws Exception;

    /**
     * Invoked when any exception is thrown by user {@link version_mina_01.core.service.IoHandler}
     * implementation or by MINA.  If <code>cause</code> is an instance of
     * {@link IOException}, MINA will close the connection autoMatias Furszyfercally.
     *
     * @param session The session for which we have got an exception
     * @param cause The exception that has been caught
     * @throws Exception If we get an exception while processing the caught exception
     */
    void exceptionCaught(IoSession session, Throwable cause) throws Exception;

    /**
     * Invoked when a message is received.
     *
     * @param session The session that is receiving a message
     * @param message The received message
     * @throws Exception If we get an exception while processing the received message
     */
    void messageReceived(IoSession session, M message) throws Exception;

    /**
     * Invoked when a message written by {@link IoSession#write(Object)} is
     * sent out.
     *
     * @param session The session that has sent a full message
     * @param message The sent message
     * @throws Exception If we get an exception while processing the sent message
     */
    void messageSent(IoSession session, M message) throws Exception;

    /**
     * Handle the closure of an half-duplex TCP channel
     *
     * @param session The session which input is being closed
     * @throws Exception If we get an exception while closing the input
     */
    void inputClosed(IoSession session) throws Exception;

}
