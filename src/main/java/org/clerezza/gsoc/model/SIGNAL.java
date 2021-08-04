package org.clerezza.gsoc.model;

import org.apache.clerezza.IRI;

public class SIGNAL {

    public static final IRI Conversation = new IRI("http://clerezza.apache.org/2021/gsoc/Conversation");

    /**
     *
     */
    public static final IRI Message = new IRI("http://clerezza.apache.org/2021/gsoc/Message");

    // Properties

    /**
     *
     */
    public static final IRI consistOf = new IRI("http://clerezza.apache.org/2021/gsoc/consistOf");

    /**
     * comment: Relationship between Person and Conversation.

     */
    public static final IRI createdBy = new IRI("http://clerezza.apache.org/2021/gsoc/createdBy");

    /**
     *
     */
    public static final IRI post = new IRI("http://clerezza.apache.org/2021/gsoc/post");

    /**
     * comment: The date and time when the conversation was created.

     */
    public static final IRI startDate = new IRI("http://clerezza.apache.org/2021/gsoc/startDate");

    /**
     *
     */
    public static final IRI text = new IRI("http://clerezza.apache.org/2021/gsoc/text");

    /**
     * comment: The date and time when the message was created.

     */
    public static final IRI timeStamp = new IRI("http://clerezza.apache.org/2021/gsoc/timeStamp");

    /**
     *
     */
    public static final IRI username = new IRI("http://clerezza.apache.org/2021/gsoc/username");

    public static final IRI conversationName = new IRI("http://clerezza.apache.org/2021/gsoc/conversationName");

    // Properties

    /**
     *
     */
    public static final IRI THIS_ONTOLOGY = new IRI("http://clerezza.apache.org/2021/gsoc/");

}
