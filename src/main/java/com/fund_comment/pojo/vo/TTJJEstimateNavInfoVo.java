package com.fund_comment.pojo.vo;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zoubin
 * @description: TODO
 * @date 2021/7/16 1:23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "FCODE",
        "SHORTNAME",
        "GZTIME",
        "GSZ",
        "GSZZL",
        "ISBUY",
        "BUY",
        "LISTTEXCH",
        "ISLISTTRADE"
})
public class TTJJEstimateNavInfoVo {

    @JsonProperty("FCODE")
    private String fcode;
    @JsonProperty("SHORTNAME")
    private String shortname;
    @JsonProperty("GZTIME")
    private String gztime;
    @JsonProperty("GSZ")
    private Double gsz;
    @JsonProperty("GSZZL")
    private Double gszzl;
    @JsonProperty("ISBUY")
    private String isbuy;
    @JsonProperty("BUY")
    private Boolean buy;
    @JsonProperty("LISTTEXCH")
    private String listtexch;
    @JsonProperty("ISLISTTRADE")
    private String islisttrade;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("FCODE")
    public String getFcode() {
        return fcode;
    }

    @JsonProperty("FCODE")
    public void setFcode(String fcode) {
        this.fcode = fcode;
    }

    @JsonProperty("SHORTNAME")
    public String getShortname() {
        return shortname;
    }

    @JsonProperty("SHORTNAME")
    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    @JsonProperty("GZTIME")
    public String getGztime() {
        return gztime;
    }

    @JsonProperty("GZTIME")
    public void setGztime(String gztime) {
        this.gztime = gztime;
    }

    @JsonProperty("GSZ")
    public Double getGsz() {
        return gsz;
    }

    @JsonProperty("GSZ")
    public void setGsz(Double gsz) {
        this.gsz = gsz;
    }

    @JsonProperty("GSZZL")
    public Double getGszzl() {
        return gszzl;
    }

    @JsonProperty("GSZZL")
    public void setGszzl(Double gszzl) {
        this.gszzl = gszzl;
    }

    @JsonProperty("ISBUY")
    public String getIsbuy() {
        return isbuy;
    }

    @JsonProperty("ISBUY")
    public void setIsbuy(String isbuy) {
        this.isbuy = isbuy;
    }

    @JsonProperty("BUY")
    public Boolean getBuy() {
        return buy;
    }

    @JsonProperty("BUY")
    public void setBuy(Boolean buy) {
        this.buy = buy;
    }

    @JsonProperty("LISTTEXCH")
    public String getListtexch() {
        return listtexch;
    }

    @JsonProperty("LISTTEXCH")
    public void setListtexch(String listtexch) {
        this.listtexch = listtexch;
    }

    @JsonProperty("ISLISTTRADE")
    public String getIslisttrade() {
        return islisttrade;
    }

    @JsonProperty("ISLISTTRADE")
    public void setIslisttrade(String islisttrade) {
        this.islisttrade = islisttrade;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TTJJEstimateNavInfoVo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("fcode");
        sb.append('=');
        sb.append(((this.fcode == null)?"<null>":this.fcode));
        sb.append(',');
        sb.append("shortname");
        sb.append('=');
        sb.append(((this.shortname == null)?"<null>":this.shortname));
        sb.append(',');
        sb.append("gztime");
        sb.append('=');
        sb.append(((this.gztime == null)?"<null>":this.gztime));
        sb.append(',');
        sb.append("gsz");
        sb.append('=');
        sb.append(((this.gsz == null)?"<null>":this.gsz));
        sb.append(',');
        sb.append("gszzl");
        sb.append('=');
        sb.append(((this.gszzl == null)?"<null>":this.gszzl));
        sb.append(',');
        sb.append("isbuy");
        sb.append('=');
        sb.append(((this.isbuy == null)?"<null>":this.isbuy));
        sb.append(',');
        sb.append("buy");
        sb.append('=');
        sb.append(((this.buy == null)?"<null>":this.buy));
        sb.append(',');
        sb.append("listtexch");
        sb.append('=');
        sb.append(((this.listtexch == null)?"<null>":this.listtexch));
        sb.append(',');
        sb.append("islisttrade");
        sb.append('=');
        sb.append(((this.islisttrade == null)?"<null>":this.islisttrade));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.gztime == null)? 0 :this.gztime.hashCode()));
        result = ((result* 31)+((this.islisttrade == null)? 0 :this.islisttrade.hashCode()));
        result = ((result* 31)+((this.gszzl == null)? 0 :this.gszzl.hashCode()));
        result = ((result* 31)+((this.isbuy == null)? 0 :this.isbuy.hashCode()));
        result = ((result* 31)+((this.listtexch == null)? 0 :this.listtexch.hashCode()));
        result = ((result* 31)+((this.buy == null)? 0 :this.buy.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.shortname == null)? 0 :this.shortname.hashCode()));
        result = ((result* 31)+((this.fcode == null)? 0 :this.fcode.hashCode()));
        result = ((result* 31)+((this.gsz == null)? 0 :this.gsz.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TTJJEstimateNavInfoVo) == false) {
            return false;
        }
        TTJJEstimateNavInfoVo rhs = ((TTJJEstimateNavInfoVo) other);
        return (((((((((((this.gztime == rhs.gztime)||((this.gztime!= null)&&this.gztime.equals(rhs.gztime)))&&((this.islisttrade == rhs.islisttrade)||((this.islisttrade!= null)&&this.islisttrade.equals(rhs.islisttrade))))&&((this.gszzl == rhs.gszzl)||((this.gszzl!= null)&&this.gszzl.equals(rhs.gszzl))))&&((this.isbuy == rhs.isbuy)||((this.isbuy!= null)&&this.isbuy.equals(rhs.isbuy))))&&((this.listtexch == rhs.listtexch)||((this.listtexch!= null)&&this.listtexch.equals(rhs.listtexch))))&&((this.buy == rhs.buy)||((this.buy!= null)&&this.buy.equals(rhs.buy))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.shortname == rhs.shortname)||((this.shortname!= null)&&this.shortname.equals(rhs.shortname))))&&((this.fcode == rhs.fcode)||((this.fcode!= null)&&this.fcode.equals(rhs.fcode))))&&((this.gsz == rhs.gsz)||((this.gsz!= null)&&this.gsz.equals(rhs.gsz))));
    }

}