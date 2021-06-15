package com.ziperp.dms

import com.ziperp.dms.camera.ImageViewModel
import com.ziperp.dms.common.table.viewmodel.TableAllViewModel
import com.ziperp.dms.core.form.combopopup.viewmodel.ComboPopupViewModel
import com.ziperp.dms.core.form.dialogpopup.viewmodel.DialogPopupViewModel
import com.ziperp.dms.core.repository.*
import com.ziperp.dms.core.rest.RestService
import com.ziperp.dms.core.service.*
import com.ziperp.dms.core.tracking.LocationRepository
import com.ziperp.dms.main.customer.customerdetail.viewmodel.CustomerDetailViewModel
import com.ziperp.dms.main.customer.customerimage.CustomerImageViewModel
import com.ziperp.dms.main.customer.customernote.viewmodel.CustomerNoteViewModel
import com.ziperp.dms.main.customer.customerroute.CustomerRouteViewModel
import com.ziperp.dms.main.customer.list.viewmodel.CustomerViewModel
import com.ziperp.dms.main.itemmaster.viewmodel.ItemMasterViewModel
import com.ziperp.dms.main.login.viewmodel.LoginViewModel
import com.ziperp.dms.main.personalreport.datasummary.viewmodel.DataSummaryViewModel
import com.ziperp.dms.main.personalreport.salesresult.viewmodel.YourSalesResultViewModel
import com.ziperp.dms.main.personalreport.timekeeping.viewmodel.TKDiaryViewModel
import com.ziperp.dms.main.personalreport.visitdetail.viewmodel.VisitDetailViewModel
import com.ziperp.dms.main.personalreport.visitresult.VisitResultViewModel
import com.ziperp.dms.main.saleorder.viewmodel.SaleOrderViewModel
import com.ziperp.dms.main.salespricepromotion.viewmodel.PromotionViewModel
import com.ziperp.dms.main.salespricepromotion.viewmodel.SalesPriceViewModel
import com.ziperp.dms.main.timekeeping.viewmodel.TimeKeepingViewModel
import com.ziperp.dms.main.trackingreports.customernotorder.CustomerNotOrderViewModel
import com.ziperp.dms.main.trackingreports.dailyperformance.viewmodel.DailyPerformanceViewModel
import com.ziperp.dms.main.trackingreports.newpoint.viewmodel.NewSalesPointViewModel
import com.ziperp.dms.main.trackingreports.picture.viewmodel.TrackingPictureViewModel
import com.ziperp.dms.main.trackingreports.reportsummation.viewmodel.ReportSummationViewModel
import com.ziperp.dms.main.trackingreports.salesonroute.viewmodel.SalesOnRouteViewModel
import com.ziperp.dms.main.trackingreports.salesresult.SalesResultViewModel
import com.ziperp.dms.main.trackingreports.staff.viewmodel.TrackingStaffViewModel
import com.ziperp.dms.main.trackingreports.visitcustomer.viewmodel.TrackingVSViewModel
import com.ziperp.dms.main.user.viewmodel.UserInfoViewModel
import com.ziperp.dms.main.visitcustomer.viewmodel.CUDItemCountViewModel
import com.ziperp.dms.main.visitcustomer.viewmodel.VisitCustomerViewModel

object Injection {
//    fun provideLocale()
//    fun provideDatabase(context: Context)

    fun provideLoginViewModel(): LoginViewModel {
        val service = RestService.createService(LoginService::class.java)
        val repository = LoginRepository(service)
        return LoginViewModel(repository)
    }

    fun provideCustomerViewModel(): CustomerViewModel {
        val service = RestService.createService(CustomerListService::class.java)
        val repository = AccountListRepository(service)
        return CustomerViewModel(repository)
    }

    fun provideCustomerDetailViewModel(): CustomerDetailViewModel {
        val service = RestService.createService(CustomerDetailService::class.java)
        val dao = App.shared().appDatabase.customerDetailDao()
        val repository = CustomerDetailRepository(service, dao)
        return CustomerDetailViewModel(repository)
    }

    fun provideTimeKeepingViewModel(): TimeKeepingViewModel {
        val service = RestService.createService(TimeKeepingService::class.java)
        val dao = App.shared().appDatabase.timeKeepingDao()
        val repository = TimeKeepingRepository(service, dao)
        return TimeKeepingViewModel(repository)
    }

    fun provideCustomerNoteViewModel(): CustomerNoteViewModel {
        val service = RestService.createService(CustomerNoteService::class.java)
        val repository = CustomerNoteRepository(service)
        return CustomerNoteViewModel(repository)
    }

    fun provideCustomerRouteViewModel(): CustomerRouteViewModel {
        val service = RestService.createService(CustomerRouteService::class.java)
        val dao = App.shared().appDatabase.customerRouteDao()
        val repository = CustomerRouteRepository(service, dao)
        return CustomerRouteViewModel(repository)
    }

    fun provideCustomerImageViewModel(): CustomerImageViewModel {
        val service = RestService.createService(CustomerImageService::class.java)
        val repository = CustomerImageRepository(service)
        return CustomerImageViewModel(repository)
    }

    fun provideItemMasterViewModel(): ItemMasterViewModel {
        val service = RestService.createService(ItemMasterService::class.java)
        val repository = ItemMasterRepository(service)
        return ItemMasterViewModel(repository)
    }

    fun provideSalesPriceViewModel(): SalesPriceViewModel {
        val service = RestService.createService(SalesPriceService::class.java)
        val repository = SalesPriceRepository(service)
        return SalesPriceViewModel(repository)
    }

    fun providePromotionViewModel(): PromotionViewModel {
        val service = RestService.createService(PromotionService::class.java)
        val repository = PromotionRepository(service)
        return PromotionViewModel(repository)
    }

    fun provideSaleOrderViewModel(): SaleOrderViewModel {
        val service = RestService.createService(SaleOrderService::class.java)
        val repository = SaleOrderRepository(service)
        return SaleOrderViewModel(repository)
    }

    fun provideTableAllViewModel(): TableAllViewModel {
        val tableService = RestService.createService(TableAllService::class.java)
        val reportService = RestService.createService(SaleReportService::class.java)
        val tableRepository = TableRepository(tableService)
        val saleRepository = SaleReportRepository(reportService)
        return TableAllViewModel(tableRepository, saleRepository)
    }

    fun provideVisitCustomerViewModel(): VisitCustomerViewModel {
        val service = RestService.createService(VisitCustomerService::class.java)
        val dao = App.shared().appDatabase.visitCustomerDao()
        val repository = VisitCustomerRepository(service, dao)
        return VisitCustomerViewModel(repository)
    }

    fun provideImageViewModel(): ImageViewModel {
        val service = RestService.createService(ImageService::class.java)
        val dao = App.shared().appDatabase.customerImageDao()
        val repository = ImageRepository(service, dao)
        return ImageViewModel(repository)
    }

    fun providerDialogPopupViewModel(): DialogPopupViewModel {
        val service = RestService.createService(DialogPopupService::class.java)
        val dao = App.shared().appDatabase.formControlDao()
        val repository = DialogPopupRepository(service, dao)
        return DialogPopupViewModel(
            repository
        )
    }

    fun providerComboPopupViewModel(): ComboPopupViewModel {
        val service = RestService.createService(ComboPopupService::class.java)
        val dao = App.shared().appDatabase.formControlDao()
        val repository = ComboPopupRepository(service, dao)
        return ComboPopupViewModel(
            repository
        )
    }

    fun provideStockCountViewModel(): CUDItemCountViewModel {
        val service = RestService.createService(StockCountService::class.java)
        val repository = StockCountRepository(service)
        return CUDItemCountViewModel(repository)
    }

    fun provideTrackingStaffViewModel(): TrackingStaffViewModel {
        val service = RestService.createService(TrackingStaffService::class.java)
        val repository = TrackingStaffRepository(service)
        return TrackingStaffViewModel(repository)
    }

    fun provideTrackingVSViewModel(): TrackingVSViewModel {
        val service = RestService.createService(TrackingVSService::class.java)
        val repository = TrackingVSRepository(service)
        return TrackingVSViewModel(repository)
    }

    fun provideTrackingPictureViewModel(): TrackingPictureViewModel {
        val service = RestService.createService(TrackingPictureService::class.java)
        val repository = TrackingPictureRepository(service)
        return TrackingPictureViewModel(repository)
    }

    fun provideCstNotOrderViewModel(): CustomerNotOrderViewModel {
        val service = RestService.createService(CstNotOrderService::class.java)
        val repository = CstNotOrderRepository(service)
        return CustomerNotOrderViewModel(repository)
    }

    fun provideSalesResultViewModel(): SalesResultViewModel {
        val service = RestService.createService(SalesResultService::class.java)
        val repository = SalesResultRepository(service)
        return SalesResultViewModel(repository)
    }

    fun provideDailyPerformanceViewModel(): DailyPerformanceViewModel {
        val service = RestService.createService(DailyPerformanceService::class.java)
        val repository = DailyPerformanceRepository(service)
        return DailyPerformanceViewModel(repository)
    }

    fun provideNewSalesPointViewModel(): NewSalesPointViewModel {
        val service = RestService.createService(NewSalesPointService::class.java)
        val repository = NewSalesPointRepository(service)
        return NewSalesPointViewModel(repository)
    }

    fun provideReportSummationViewModel(): ReportSummationViewModel {
        val service = RestService.createService(ReportSummationService::class.java)
        val repository = ReportSummationRepository(service)
        return ReportSummationViewModel(repository)
    }

    fun provideSalesOnRouteViewModel(): SalesOnRouteViewModel {
        val service = RestService.createService(SalesOnRouteService::class.java)
        val repository = SalesOnRouteRepository(service)
        return SalesOnRouteViewModel(repository)
    }

    fun provideVisitResultViewModel(): VisitResultViewModel {
        val service = RestService.createService(VisitResultService::class.java)
        val repository = VisitResultRepository(service)
        return VisitResultViewModel(repository)
    }

    fun provideVisitDetailViewModel(): VisitDetailViewModel {
        val service = RestService.createService(VisitDetailService::class.java)
        val repository = VisitDetailRepository(service)
        return VisitDetailViewModel(repository)
    }

    fun provideTKDiaryViewModel(): TKDiaryViewModel {
        val service = RestService.createService(TKDiaryService::class.java)
        val repository = TKDiaryRepository(service)
        return TKDiaryViewModel(repository)
    }

    fun provideYourSalesResultViewModel(): YourSalesResultViewModel {
        val service = RestService.createService(YourSalesResultService::class.java)
        val repository = YourSalesResultRepository(service)
        return YourSalesResultViewModel(repository)
    }

    fun provideDataSummaryViewModel(): DataSummaryViewModel {
        val service = RestService.createService(DataSummaryService::class.java)
        val repository = DataSummaryRepository(service)
        return DataSummaryViewModel(repository)
    }

    fun provideUserInfoViewModel(): UserInfoViewModel {
        val service = RestService.createService(UserInfoService::class.java)
        val repository = UserInfoRepository(service)
        return UserInfoViewModel(repository)
    }


    //Repository
    fun provideCustomerRouteRepository(): CustomerRouteRepository {
        val service = RestService.createService(CustomerRouteService::class.java)
        val dao = App.shared().appDatabase.customerRouteDao()
        return CustomerRouteRepository(service, dao)
    }

    fun provideCustomerImageRepository(): ImageRepository {
        val service = RestService.createService(ImageService::class.java)
        val dao = App.shared().appDatabase.customerImageDao()
        return ImageRepository(service, dao)
    }

    fun provideCustomerDetailRepository(): CustomerDetailRepository {
        val service = RestService.createService(CustomerDetailService::class.java)
        val dao = App.shared().appDatabase.customerDetailDao()
        return CustomerDetailRepository(service, dao)
    }

    fun provideVisitCustomerRepository(): VisitCustomerRepository {
        val service = RestService.createService(VisitCustomerService::class.java)
        val dao = App.shared().appDatabase.visitCustomerDao()
        return VisitCustomerRepository(service, dao)
    }

    fun provideTimeKeepingRepository(): TimeKeepingRepository {
        val service = RestService.createService(TimeKeepingService::class.java)
        val dao = App.shared().appDatabase.timeKeepingDao()
        return TimeKeepingRepository(service, dao)
    }

    fun provideLocationRepository(): LocationRepository {
        val service = RestService.createService(TrackingService::class.java)
        val dao = App.shared().appDatabase.trackingRequestDao()
        return LocationRepository(service, dao)
    }


}