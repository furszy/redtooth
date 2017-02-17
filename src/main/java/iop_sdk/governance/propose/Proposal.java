package iop_sdk.governance.propose;

import com.google.protobuf.ByteString;

import org.bitcoinj.core.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import iop_sdk.global.exceptions.NotValidParametersException;

import static iop_sdk.global.utils.Preconditions.checkEquals;
import static iop_sdk.governance.ProposalForum.TAG_BENEFICIARY_ADDRESS;
import static iop_sdk.governance.ProposalForum.TAG_BENEFICIARY_VALUE;
import static iop_sdk.governance.ProposalForum.TAG_BLOCK_REWARD;
import static iop_sdk.governance.ProposalForum.TAG_BODY;
import static iop_sdk.governance.ProposalForum.TAG_END_BLOCK;
import static iop_sdk.governance.ProposalForum.TAG_START_BLOCK;
import static iop_sdk.governance.ProposalForum.TAG_SUBTITLE;
import static iop_sdk.governance.ProposalForum.TAG_TITLE;


/**
 * Created by mati on 07/11/16.
 * //todo: ponerle el class en el tag para determinar que campo es.
 * // ver tema fee extra, --> fee minimo es de 1 IoP para evitar spam
 */

public class Proposal implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Proposal.class);

    public static final int BLOCK_REWARD_MAX_VALUE = 10000000;
    public static final double START_BLOCK_MAX_VALUE = Math.pow((double) 2, (double) 24);
    public static final double END_BLOCK_MAX_VALUE = 120960;


    public enum ProposalState{
        DRAFT(100),          // Proposal in a edit state
        FORUM(101),          // Proposal created and posted in the forum
        PENDING(102),        // Proposal is waiting to be included in a block
        SUBMITTED(103),      //  Transaction confirmed on blockchain. No votes yet.
        APPROVED(104),       // YES > NO. Current height  < (BlockStart + 1000 blocks).
        NOT_APPROVED(105), 			// NO > YES. Current height  < (BlockStart + 1000 blocks).
        CANCELED_BY_OWNER(106),  // Proposal canceled by the owner, moving the locked funds to another address.
        // todo: faltan estados..
        QUEUED_FOR_EXECUTION(107),	// YES > NO. Current height  < (BlockStart + 1000 blocks).
        IN_EXECUTION(108),			// YES > NO. Current height  > BlockStart  and Current height  < BlockEnd
        QUEUED(109),                     //
        EXECUTION_CANCELLED(110), 	// NO > YES. Current height  > BlockStart  and Current height  < BlockEnd
        EXECUTED(111),				// YES > NO. Current height  > BlockEnd
        UNKNOWN(112);

        private int id;

        ProposalState(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }




    private boolean isMine;
    private boolean isSent;
    private String genesisTxHash;
    private long lockedOutputIndex;
    private ProposalState state = ProposalState.DRAFT;
    private long voteYes;
    private long voteNo;

    // IoPIP -> IoP improvement proposal
    private short version =  0x0100;
    private String title;
    private String subTitle;
    private String category;
    private String body;
    private int startBlock;
    private int endBlock;
    private long blockReward;
    private int pendingBlocks = -1;
    /** topic id */
    private int forumId;
    /** post id */
    private int forumPostId;
    // address -> value
    private List<Beneficiary> beneficiaries;
    /** Contributor owner */
    private byte[] ownerPubKey;
    /** will be used to put the proposal upper or lower in the voters list */
    private long extraFeeValue = 100000000;

    public static Proposal buildRandomProposal(){
        Proposal proposal = new Proposal();
        proposal.setMine(true);
        proposal.setSent(false);
        proposal.setState(ProposalState.DRAFT);
        proposal.setTitle("Propuesta a enviar numero 1011");
        proposal.setSubTitle("subTitulo4");
        proposal.setCategory("categoria");
        proposal.setBody("Esta es una propuesta para crear propuestas, por lo cual debo poner cosas locas acá. Asi que voy a poner lo que se me dé la gana. y voy a llenar todo esto con cosas para probar su funcionamiento es bueno");
        proposal.setStartBlock(10);
        proposal.setEndBlock(10);
        proposal.setBlockReward(8000000);
        return proposal;
    }

    public static Proposal buildRandomProposal(String title){
        Proposal proposal = new Proposal();
        proposal.setMine(true);
        proposal.setSent(false);
        proposal.setState(ProposalState.DRAFT);
        proposal.setTitle(title);
        proposal.setSubTitle("subTitulo4");
        proposal.setCategory("categoria");
        proposal.setBody("Esta es una propuesta para crear propuestas, por lo cual debo poner cosas locas acá. Asi que voy a poner lo que se me dé la gana. y voy a llenar todo esto con cosas para probar su funcionamiento es bueno");
        proposal.setStartBlock(10);
        proposal.setEndBlock(100);
        proposal.setBlockReward(10000000);
        return proposal;
    }

    /**
     * StringBuilder stringBuilder = new StringBuilder()
     .append("<h1>"+title+"</h1>")
     .append("<h2>"+subTitle+"</h2>")
     .append("<br/>")
     .append("<p><body>"+body+"</body></p>")
     .append("Start block: <startBlock>"+startBlock+"</startBlock>")
     .append("   ")
     .append("EndBlock: <endBlock>"+endBlock+"</endBlock>")
     .append("<br/>")
     .append("Block reward <blockReward>"+blockReward+"</blockReward>")
     .append("<br/>");

     int pos = 1;
     for (Map.Entry<String, Long> beneficiary : beneficiaries.entrySet()) {
     stringBuilder.append("Beneficiary"+pos+": Address: <address>"+beneficiary.getKey()+"</address>   value: <value>"+beneficiary.getValue()+"</value> IoPs");
     }

     return stringBuilder.toString();
     * @param formatedBody
     */
    public static Proposal buildFromBody(String formatedBody) {
        LOG.info("buildFromBody: "+formatedBody);
        Proposal proposal = new Proposal();
        proposal.setTitle(getTagValue(formatedBody, PATTERN_TAG_TITLE,0));
        proposal.setSubTitle(getTagValue(formatedBody, PATTERN_TAG_SUBTITLE,0));
        proposal.setBody(getTagValue(formatedBody,PATTERN_TAG_BODY,0));
        proposal.setStartBlock(Integer.parseInt(getTagValue(formatedBody,PATTERN_TAG_START_BLOCK,1)));
        proposal.setEndBlock(Integer.parseInt(getTagValue(formatedBody,PATTERN_TAG_END_BLOCK,2)));
        proposal.setBlockReward(Long.parseLong(getTagValue(formatedBody,PATTERN_TAG_BLOCK_REWARD,3)));
//        String address = getTagValue(formatedBody,PATTERN_TAG_BENEFICIARY_ADDRESS,4);
//        long value = Long.parseLong(getTagValue(formatedBody,PATTERN_TAG_BENEFICIARY_VALUE,5));
//        proposal.addBeneficiary(address,value);
//
        proposal.setBeneficiaries(buildBeneficiaries(formatedBody));
        return proposal;
    }

    public static List<Beneficiary> buildBeneficiaries(String formatedBody){
        List<Beneficiary> beneficiaries = new ArrayList<>();
        List<String> list = getTagValues(formatedBody,PATTERN_TAG_BENEFICIARY_ADDRESS);
        if (!list.isEmpty()){
            for (int i=4;i<list.size();i=i+2){
                beneficiaries.add(new Beneficiary(list.get(i),Long.parseLong(list.get(i+1))));
            }
        }else
            LOG.info("Tag not found for pattern: "+PATTERN_TAG_BENEFICIARY_ADDRESS.toString());
        return beneficiaries;
    }

    private static final Pattern PATTERN_TAG_TITLE = Pattern.compile(replaceTag(TAG_TITLE,"(.+?)"));//("<h1>(.+?)</h1>");
    private static final Pattern PATTERN_TAG_SUBTITLE = Pattern.compile(replaceTag(TAG_SUBTITLE,"(.+?)"));//("<h2>(.+?)</h2>");
    private static final Pattern PATTERN_TAG_BODY = Pattern.compile(replaceTag(TAG_BODY,"(.+?)"));//("<contract_body>(.+?)</contract_body>");
    private static final Pattern PATTERN_TAG_START_BLOCK = Pattern.compile(replaceTag(TAG_START_BLOCK,"(.+?)"));//("<startBlock>(.+?)</startBlock>");
    private static final Pattern PATTERN_TAG_END_BLOCK = Pattern.compile(replaceTag(TAG_END_BLOCK,"(.+?)"));//("<endBlock>(.+?)</endBlock>");
    private static final Pattern PATTERN_TAG_BLOCK_REWARD = Pattern.compile(replaceTag(TAG_BLOCK_REWARD,"(.+?)"));//("<blockReward>(.+?)</blockReward>");
    private static final Pattern PATTERN_TAG_BENEFICIARY_ADDRESS = Pattern.compile(replaceTag(TAG_BENEFICIARY_ADDRESS,"(.+?)"));//("<address>(.+?)</address>");
    private static final Pattern PATTERN_TAG_BENEFICIARY_VALUE = Pattern.compile(replaceTag(TAG_BENEFICIARY_VALUE,"(.+?)"));//("<value>(.+?)</value>");

    private static String replaceTag(String tag,String replace){
        return tag.replace("?",replace);
    }

    private static List<String> getTagValues(final String str, Pattern pattern) {
        final List<String> tagValues = new ArrayList<String>();
        final Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            tagValues.add(matcher.group(1));
        }
        return tagValues;
    }

    private static String getTagValue(final String str,Pattern pattern, int index){
        List<String> list = getTagValues(str,pattern);
        if (!list.isEmpty()){
            return list.get(index);
        }else
            LOG.info("Tag not found for pattern: "+pattern.toString());
        return null;
    }

    public Proposal(){
        beneficiaries = new ArrayList<>();
    }

    public Proposal(boolean isMine, String title, String subTitle, String category,String body, int startBlock, int endBlock, long blockReward, int forumId, List<Beneficiary> beneficiaries,long extraFeeValue,boolean isSent,String lockedOutputHashhex,long lockedOutputIndex,short version,byte[] ownerPk,ProposalState proposalState) {
        this.isMine = isMine;
        this.title = title;
        this.subTitle = subTitle;
        this.category = category;
        this.body = body;
        this.startBlock = startBlock;
        this.endBlock = endBlock;
        this.blockReward = blockReward;
        this.forumId = forumId;
        this.beneficiaries = beneficiaries;
        this.extraFeeValue = extraFeeValue;
        this.isSent = isSent;
        this.genesisTxHash = lockedOutputHashhex;
        this.lockedOutputIndex = lockedOutputIndex;
        this.version = version;
        this.ownerPubKey = ownerPk;
        this.state = proposalState;
    }

    public byte[] hash(){


        ByteString buffTitle = ByteString.copyFromUtf8(this.title);
        ByteString buffSubTitle = ByteString.copyFromUtf8(this.subTitle);
        ByteString buffBody = ByteString.copyFromUtf8(this.body);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4048);
        byteBuffer.put(buffTitle.toByteArray());
        byteBuffer.put(buffSubTitle.toByteArray());
        byteBuffer.put(buffBody.toByteArray());
        byteBuffer.putInt(startBlock);
        byteBuffer.putInt(endBlock);
        byteBuffer.putLong(blockReward);

        int position = byteBuffer.position();
        byte[] buffToHash = new byte[position];
        byteBuffer.get(buffToHash,0,position);

        return Sha256Hash.hash(buffToHash);
    }

    /**
     * Check hash, esto se va a hacer cuando setee los datos del foro el la propuesta decodificada de la blockchain
     * @return
     */
    public boolean checkHash(Proposal proposal) {
        if (proposal!=null){
            return Arrays.equals(proposal.hash(),hash());
        }
        return false;
    }

    public String buildExtraData() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appendFieldsWithName("title",title));
        stringBuilder.append(appendFieldsWithName("subtitle",subTitle));
        stringBuilder.append(appendFieldsWithName("category",category));
        stringBuilder.append(appendFieldsWithName("body",body));
        stringBuilder.append(appendFieldsWithName("startBlock",startBlock));
        stringBuilder.append(appendFieldsWithName("endBlockHeight",endBlock));
        stringBuilder.append(appendFieldsWithName("blockReward",blockReward));
//        stringBuilder.append(appendFieldsWithName("forumLink",forumLink));
        stringBuilder.append(appendFieldsWithName("version",version));

        return stringBuilder.toString();
    }

    public String toForumBody(){

        StringBuilder stringBuilder = new StringBuilder()
                        .append(replaceTag(TAG_TITLE,title))//"<h1>"+title+"</h1>")
                        .append(replaceTag(TAG_SUBTITLE,subTitle))//"<h2>"+subTitle+"</h2>")
                        .append("<br/>")
                        .append("<p><body>"+replaceTag(TAG_BODY,body)+"</body></p>")
                        .append("Start block: "+replaceTag(TAG_START_BLOCK, String.valueOf(startBlock)))
                        .append("   ")
                        .append("EndBlock: "+replaceTag(TAG_END_BLOCK, String.valueOf(endBlock)))
                        .append("<br/>")
                        .append("Block reward "+replaceTag(TAG_BLOCK_REWARD, String.valueOf(blockReward)))
                        .append("<br/>");


        for (int i = 0; i < beneficiaries.size(); i++) {
            Beneficiary beneficiary = beneficiaries.get(i);
            if (i!=0){
                stringBuilder.append("\n");
            }
            stringBuilder.append("Beneficiary"+(i+1)+": Address: <address>"+replaceTag(TAG_BENEFICIARY_ADDRESS,beneficiary.getAddress())+"</address>   value: "+replaceTag(TAG_BENEFICIARY_VALUE, String.valueOf(beneficiary.getAmount()))+" IoPs");
        }


        return stringBuilder.toString();

    }


    private String appendFieldsWithName(String key,Object value){
        return key+"="+value;
    }

    public void addBeneficiary(String address,long value){
        beneficiaries.add(new Beneficiary(address,value));
    }
    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getCategory() {
        return category;
    }

    public int getStartBlock() {
        return startBlock;
    }

    public int getEndBlock() {
        return endBlock;
    }

    public long getBlockReward() {
        return blockReward;
    }

    public List<Beneficiary> getBeneficiaries() {
        return beneficiaries;
    }

    public long getExtraFeeValue() {
        return extraFeeValue;
    }

    public String getBody() {
        return body;
    }

    public short getVersion() {
        return version;
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isSent() {
        return isSent;
    }

    public String getGenesisTxHash() {
        return genesisTxHash;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public void setGenesisTxHash(String genesisTxHash) {
        this.genesisTxHash = genesisTxHash;
    }

    public int getForumId() {
        return forumId;
    }


    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setStartBlock(int startBlock) {
        if (startBlock>START_BLOCK_MAX_VALUE) throw new IllegalArgumentException("Start block must be lower than "+START_BLOCK_MAX_VALUE);
        this.startBlock = startBlock;
    }

    public void setEndBlock(int endBlock) {
        if(endBlock>END_BLOCK_MAX_VALUE) throw new IllegalArgumentException("End block must be lower than "+END_BLOCK_MAX_VALUE);
        this.endBlock = endBlock;
    }

    public void setBlockReward(long blockReward) {
        if (blockReward>BLOCK_REWARD_MAX_VALUE)throw new IllegalArgumentException("Block reward must be lower than "+BLOCK_REWARD_MAX_VALUE);
        this.blockReward = blockReward;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public void setBeneficiaries(List<Beneficiary> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public void setExtraFeeValue(long extraFeeValue) {
        this.extraFeeValue = extraFeeValue;
    }

    public long getLockedOutputIndex() {
        return lockedOutputIndex;
    }

    public void setLockedOutputIndex(int lockedOutputIndex) {
        this.lockedOutputIndex = lockedOutputIndex;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public void setOwnerPubKey(byte[] ownerPubKey) {
        this.ownerPubKey = ownerPubKey;
    }

    public byte[] getOwnerPubKey() {
        return ownerPubKey;
    }

    public ProposalState getState() {
        return state;
    }

    public void setState(ProposalState state) {
        this.state = state;
    }

    public int getForumPostId() {
        return forumPostId;
    }

    public void setForumPostId(int forumPostId) {
        this.forumPostId = forumPostId;
    }

    public long getVoteYes() {
        return voteYes;
    }

    public void setVoteYes(long voteYes) {
        this.voteYes = voteYes;
    }

    public long getVoteNo() {
        return voteNo;
    }

    public void setVoteNo(long voteNo) {
        this.voteNo = voteNo;
    }

    public int getPendingBlocks() {
        return pendingBlocks;
    }

    public void setPendingBlocks(int pendingBlocks) {
        this.pendingBlocks = pendingBlocks;
    }

    public boolean isActive(){
        return (state != ProposalState.EXECUTED && state != ProposalState.EXECUTION_CANCELLED  && state != ProposalState.CANCELED_BY_OWNER);
    }

    public boolean equals(Proposal o2) throws NotValidParametersException {
        checkEquals(getTitle(),o2.getTitle(),"tittle is changed");
        checkEquals(getSubTitle(),o2.getSubTitle(),"Subtitle is changed");
        checkEquals(getBody(),o2.getBody(),"Body is changed");
        checkEquals(getStartBlock(),o2.getStartBlock(),"StartBlock is changed");
        checkEquals(getEndBlock(),o2.getEndBlock(),"EndBlock is changed");
        checkEquals(getBlockReward(),o2.getBlockReward(),"BlockReward is changed");
        for (int i = 0; i < beneficiaries.size(); i++) {
            if (!beneficiaries.get(i).getAddress().equals(o2.getBeneficiaries().get(i).getAddress())) throw new NotValidParametersException("Beneficiary address is changed");
            if (beneficiaries.get(i).getAmount() != (o2.getBeneficiaries().get(i).getAmount())) throw new NotValidParametersException("Beneficiary value is changed");
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Proposal)) return false;
        else {
            if (this.getForumId()==((Proposal) o).getForumId()){
                return true;
            }else {
                return false;
            }
        }
    }

    public String toStringBlockchain(){
        return  "Proposal{" +
                ", startBlock=" + startBlock +
                ", endBlock=" + endBlock +
                ", blockReward=" + blockReward +
                ", forumId=" + forumId +
                ", blockchainHash="+ genesisTxHash+
                '}';
    }

    @Override
    public String toString() {
        return "Proposal{" +
                "isMine=" + isMine +
                ", isSent=" + isSent +
                ", lockedOutputHash=" + genesisTxHash +
                ", lockedOutputIndex=" + lockedOutputIndex +
                ", state=" + state +
                ", version=" + version +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", category='" + category + '\'' +
                ", body='" + body + '\'' +
                ", startBlock=" + startBlock +
                ", endBlock=" + endBlock +
                ", blockReward=" + blockReward +
                ", forumId=" + forumId +
                ", beneficiaries=" + beneficiaries +
                ", ownerPubKey=" + Arrays.toString(ownerPubKey) +
                ", extraFeeValue=" + extraFeeValue +
                ", pendingBlocks" + pendingBlocks +
                '}';
    }
}
