package com.destructo.sushi.ui.preferences.donation

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.android.billingclient.api.*
import com.destructo.sushi.*
import com.destructo.sushi.databinding.ActivityDonationBinding
import com.destructo.sushi.enum.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DonationActivity : AppCompatActivity(), PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private lateinit var binding: ActivityDonationBinding

    @Inject
    lateinit var sharedPref: SharedPreferences

    private lateinit var toolbar: Toolbar
    private val skuList = listOf(DONATE_COOKIE_ID, DONATE_COKE_ID, DONATE_BEER_ID,
        DONATE_COFFEE_ID, DONATE_LUNCH_ID, DONATE_GIFT_ID)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentTheme = sharedPref.getString(CURRENT_THEME, AppTheme.LIGHT.value)
        setApplicationTheme(currentTheme)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_donation)

        createBillingClient()
        initiateBillingProcess()

        toolbar = binding.donationToolbar
        setupToolbar()

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
                    billingClient.queryPurchases(BillingClient.SkuType.INAPP)
                }
            }

            override fun onBillingServiceDisconnected() {
                Timber.e("Billing service disconnected")
            }

        })
    }


    fun queryProducts(){

        val params = SkuDetailsParams
            .newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            if (skuDetailsList != null) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList.isNotEmpty()){
                    for (skuDetail in skuDetailsList) {
                        if (skuDetail.sku == DONATE_COOKIE_ID){
                            binding.cookiePrice.text = skuDetail.price
                            binding.cookieCard.setOnClickListener {
                                launchPurchaseFlow(skuDetail)
                            }
                        }
                        if (skuDetail.sku == DONATE_COKE_ID){
                            binding.cokePrice.text = skuDetail.price
                            binding.cokeCard.setOnClickListener {
                                launchPurchaseFlow(skuDetail)
                            }
                        }
                        if (skuDetail.sku == DONATE_COFFEE_ID){
                            binding.coffeePrice.text = skuDetail.price
                            binding.coffeeCard.setOnClickListener {
                                launchPurchaseFlow(skuDetail)
                            }
                        }
                        if (skuDetail.sku == DONATE_BEER_ID){
                            binding.beerPrice.text = skuDetail.price
                            binding.beerCard.setOnClickListener {
                                launchPurchaseFlow(skuDetail)
                            }
                        }
                        if (skuDetail.sku == DONATE_LUNCH_ID){
                            binding.lunchPrice.text = skuDetail.price
                            binding.lunchCard.setOnClickListener {
                                launchPurchaseFlow(skuDetail)
                            }
                        }
                        if (skuDetail.sku == DONATE_GIFT_ID){
                            binding.giftPrice.text = skuDetail.price
                            binding.giftCard.setOnClickListener {
                                launchPurchaseFlow(skuDetail)
                            }
                        }
                    }
                }

            } else {
                Toast.makeText(this, "Error retrieving products.", Toast.LENGTH_LONG).show()
                Timber.e("No Sku Found")
            }
        }
    }

    private fun launchPurchaseFlow(skuDetails: SkuDetails){
        val flowParams = BillingFlowParams
            .newBuilder()
            .setSkuDetails(skuDetails)
            .build()
        val responseCode = billingClient.launchBillingFlow(this, flowParams)
        Timber.e("Launch purchase flow result: $responseCode")
    }

    private fun handlePurchase(purchase: Purchase){
        val consumeParams = ConsumeParams
            .newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.consumeAsync(consumeParams) { billingResult, _ ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK){
                Toast.makeText(this,
                    getString(R.string.donation_succesfull),
                    Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setupToolbar(){
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if(billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null){
            for (purchase in purchases){
                handlePurchase(purchase)
            }
        }else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED){
            Timber.e("User cancelled the purchase flow.")
        }
    }

    private fun setApplicationTheme(theme: String?){
        when(theme){
            AppTheme.LIGHT.value ->{setTheme(R.style.AppTheme)}
            AppTheme.DARK.value ->{setTheme(R.style.AppTheme_Dark)}
            AppTheme.NIGHT.value ->{setTheme(R.style.AppTheme_Night)}
            AppTheme.AMOLED.value ->{setTheme(R.style.AppTheme_Amoled)}
            else ->{setTheme(R.style.AppTheme)}
        }
    }


}