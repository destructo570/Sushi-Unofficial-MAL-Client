package com.destructo.sushi.ui.preferences.donation

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.android.billingclient.api.*
import com.destructo.sushi.*
import com.destructo.sushi.databinding.ActivityDonationBinding
import com.destructo.sushi.enum.AppTheme
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DonationActivity : AppCompatActivity(), PurchasesUpdatedListener {

    private lateinit var billingClient: BillingClient
    private lateinit var binding: ActivityDonationBinding
    private lateinit var cookieButton: MaterialCardView
    private lateinit var cokeButton: MaterialCardView
    private lateinit var coffeButton: MaterialCardView
    private lateinit var beerButton: MaterialCardView
    private lateinit var lunchButton: MaterialCardView
    private lateinit var giftButton: MaterialCardView
    private lateinit var cookiePrice: TextView
    private lateinit var coffeePrice: TextView
    private lateinit var cokePrice: TextView
    private lateinit var beerPrice: TextView
    private lateinit var lunchPrice: TextView
    private lateinit var giftPrice: TextView

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

        cookieButton = binding.cookieCard
        coffeButton = binding.coffeeCard
        cokeButton = binding.cokeCard
        beerButton = binding.beerCard
        lunchButton = binding.lunchCard
        giftButton = binding.giftCard
        cookiePrice = binding.cookiePrice
        coffeePrice = binding.coffeePrice
        cokePrice = binding.cokePrice
        beerPrice = binding.beerPrice
        lunchPrice = binding.lunchPrice
        giftPrice = binding.giftPrice


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
                            cookiePrice.text = skuDetail.price
                            cookieButton.setOnClickListener {
                                launchPurchaseFlow(skuDetail)
                            }
                        }
                        if (skuDetail.sku == DONATE_COKE_ID){
                            cokePrice.text = skuDetail.price
                            cokeButton.setOnClickListener {
                                launchPurchaseFlow(skuDetail)
                            }
                        }
                        if (skuDetail.sku == DONATE_COFFEE_ID){
                            coffeePrice.text = skuDetail.price
                            coffeButton.setOnClickListener {
                                launchPurchaseFlow(skuDetail)
                            }
                        }
                        if (skuDetail.sku == DONATE_BEER_ID){
                            beerPrice.text = skuDetail.price
                            beerButton.setOnClickListener {
                                launchPurchaseFlow(skuDetail)
                            }
                        }
                        if (skuDetail.sku == DONATE_LUNCH_ID){
                            lunchPrice.text = skuDetail.price
                            lunchButton.setOnClickListener {
                                launchPurchaseFlow(skuDetail)
                            }
                        }
                        if (skuDetail.sku == DONATE_GIFT_ID){
                            giftPrice.text = skuDetail.price
                            giftButton.setOnClickListener {
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