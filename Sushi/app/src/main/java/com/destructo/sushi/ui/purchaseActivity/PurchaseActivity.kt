package com.destructo.sushi.ui.purchaseActivity

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.android.billingclient.api.*
import com.destructo.sushi.IS_PRO_USER
import com.destructo.sushi.PRODUCT_ID
import com.destructo.sushi.R
import com.destructo.sushi.databinding.ActivityPurchaseBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class PurchaseActivity : AppCompatActivity(), PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private lateinit var sharedPref: SharedPreferences
    private lateinit var restoreButton:Button
    private lateinit var purchaseButton:Button
    private lateinit var imagePagerAdapter:ImagePagerAdapter
    private lateinit var promoViewPager: ViewPager2
    private lateinit var binding:ActivityPurchaseBinding
    private val  sushiSkuMap = mutableMapOf<String, SkuDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_purchase)

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.BLACK
            }

        createBillingClient()
        initiateBillingProcess()

        promoViewPager = binding.promoImagesViewPager
        restoreButton = binding.restorePurchaseButton
        purchaseButton = binding.purchaseButton
        restoreButton.isEnabled = false
        purchaseButton.isEnabled = false

        val images = listOf(
            R.drawable.test_img,
            R.drawable.ic_add_fill,
            R.drawable.ic_bar_chart_fill,
            R.drawable.ic_anime_line
        )
        imagePagerAdapter = ImagePagerAdapter(images)
        promoViewPager.adapter = imagePagerAdapter

        restoreButton.setOnClickListener { queryPurchases() }
        purchaseButton.setOnClickListener {
            if (billingClient.isReady && sushiSkuMap.containsKey(PRODUCT_ID)){
                sushiSkuMap[PRODUCT_ID]?.let { it1 -> launchPurchaseFlow(it1) }
            }

        }

    }

    private fun createBillingClient(){
        billingClient = BillingClient
            .newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()
    }

    private fun initiateBillingProcess(){
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK){
                    Timber.e("Billing service successfully setup")
                    queryProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                Timber.e("Billing service disconnected")
            }

        })
    }

    private fun queryPurchases(){
        if(!billingClient.isReady){
            Timber.e("Billing Client Not Ready")
        }

        val result = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        if (result.purchasesList.isNullOrEmpty()){
            Toast.makeText(this,"No existing purchases found.", Toast.LENGTH_LONG).show()
        }else{
            Timber.e("Existing Purchases: ${result.purchasesList}")
            val purchaseList = result.purchasesList
            purchaseList?.let {
                for (purchase in purchaseList){
                    if (purchase.sku == PRODUCT_ID
                        && purchase.isAcknowledged
                        && purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                        ){
                        sharedPref.edit()?.putBoolean(IS_PRO_USER, true)?.apply()
                        Toast.makeText(this,"Purchase restored! Please restart the app.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    fun queryProducts(){
        val skuList = listOf(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)

        billingClient.querySkuDetailsAsync(params.build()
        ) { billingResult, skuDetails ->
            if (skuDetails != null) {
                for (skuDetail in skuDetails) {
                        sushiSkuMap[skuDetail.sku] = skuDetail
                }
                restoreButton.isEnabled = true
                purchaseButton.isEnabled = true
            } else {
                Toast.makeText(this, "Error retrieving products.",Toast.LENGTH_LONG).show()
                Timber.e("No Sku Found")
            }
        }

    }

    fun buyDirect(){
        val skuList = listOf(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)

        billingClient.querySkuDetailsAsync(params.build()
        ) { billingResult, skuDetails ->
            if (skuDetails != null) {
                for (skuDetail in skuDetails) {
                    if (skuDetail.sku == PRODUCT_ID){
                        launchPurchaseFlow(skuDetail)
                    }
                }
                restoreButton.isEnabled = true
                purchaseButton.isEnabled = true
            } else {
                Toast.makeText(this, "Error retrieving products.",Toast.LENGTH_LONG).show()
                Timber.e("No Sku Found")
            }
        }

    }

    private fun launchPurchaseFlow(skuDetails: SkuDetails){
        val flowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        val responseCode = billingClient.launchBillingFlow(this, flowParams)
        Timber.e("Launch purchase flow result: $responseCode")
    }

    private fun handlePurchase(purchase: Purchase){
        GlobalScope.launch {
            if (purchase.purchaseState  == Purchase.PurchaseState.PURCHASED){
                if (!purchase.isAcknowledged){
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
                }
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if(billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null){
            for (purchase in purchases){
                handlePurchase(purchase)
            }
        }else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED){
            Timber.e("User cancelled the purchase flow.")
        }else{
            Timber.e("onPurchaseUpdatedError : ${billingResult.responseCode}")
        }
    }

}