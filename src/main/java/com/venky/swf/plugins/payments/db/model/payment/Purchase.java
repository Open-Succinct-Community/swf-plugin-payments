package com.venky.swf.plugins.payments.db.model.payment;

import com.venky.core.util.Bucket;
import com.venky.swf.db.annotations.column.COLUMN_DEF;
import com.venky.swf.db.annotations.column.IS_NULLABLE;
import com.venky.swf.db.annotations.column.IS_VIRTUAL;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.column.defaulting.StandardDefault;
import com.venky.swf.db.annotations.column.pm.PARTICIPANT;
import com.venky.swf.db.annotations.column.validations.Enumeration;
import com.venky.swf.db.annotations.model.MENU;
import com.venky.swf.db.model.Model;
import com.venky.swf.db.model.User;
import com.venky.swf.plugins.audit.db.model.AUDITED;
import com.venky.swf.plugins.collab.db.model.CompanyNonSpecific;

import java.sql.Date;
import java.sql.Timestamp;

@MENU("Platform Revenue")
@AUDITED
public interface Purchase extends Model, CompanyNonSpecific {
    
    @PARTICIPANT
    @IS_NULLABLE
    @UNIQUE_KEY(allowMultipleRecordsWithNull = false)
    public Long getApplicationId();
    public void setApplicationId(Long id);
    public Application getApplication();
    
    @PARTICIPANT
    @IS_NULLABLE
    //Already part of UNIQUE_KEY allow multiple false via CompanyNonSpecific
    Long getCompanyId();
    void setCompanyId(Long id);
    Company getCompany();
    
    @IS_NULLABLE(false)
    @UNIQUE_KEY
    public Long getPlanId();
    public void setPlanId(Long id);
    public Plan getPlan();
    
    @UNIQUE_KEY
    @IS_NULLABLE
    Boolean isSingleUsePlan();
    void setSingleUsePlan(Boolean singleUsePlan);
    

    @Enumeration(enumClass = "com.venky.swf.plugins.payments.db.model.payment.Purchase$PaymentStatus")
    @IS_NULLABLE
    public String getStatus();
    public void setStatus(String status);


    public Bucket getRemainingCredits();
    public void setRemainingCredits(Bucket remainingCredits);

    public Timestamp getPurchasedOn();
    public void setPurchasedOn(Timestamp purchasedOn);

    @UNIQUE_KEY(allowMultipleRecordsWithNull = false)
    public String getPaymentReference();
    public void setPaymentReference(String paymentReference);

    public Long getPaymentMadeById();
    public void setPaymentMadeById(Long id);
    public User getPaymentMadeBy();

    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    public boolean isCaptured();
    public void setCaptured(boolean captured);


    public enum  PaymentStatus {
        created,
        authorized,
        failed,
        captured,
        refunded,
    }

    Date getEffectiveFrom();
    void setEffectiveFrom(Date effectiveFrom);


    Date getExpiresOn();
    void setExpiresOn(Date expiresOn);
    
    
    
    @IS_VIRTUAL
    Long getBuyerId();
    Buyer getBuyer();
    
    @COLUMN_DEF(StandardDefault.BOOLEAN_FALSE)
    @IS_NULLABLE(false)
    @UNIQUE_KEY
    boolean isProduction();
    void setProduction(boolean production);
    
    
    String getAuditRemarks();
    void setAuditRemarks(String lastUpdateRemarks);
    
    @IS_VIRTUAL
    boolean isExpired();
    
}
