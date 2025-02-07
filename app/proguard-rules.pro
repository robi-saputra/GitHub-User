# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn hilt_aggregated_deps._robi_technicaltest_networks_di_NetworkModule
-dontwarn javax.lang.model.element.Modifier
-dontwarn robi.technicaltest.baseapplication.BaseActivity
-dontwarn robi.technicaltest.baseapplication.BaseFragment
-dontwarn robi.technicaltest.baseapplication.BaseRecyclerViewAdapter$BaseViewHolder
-dontwarn robi.technicaltest.baseapplication.BaseRecyclerViewAdapter
-dontwarn robi.technicaltest.baseapplication.utils.StringUtilsKt
-dontwarn robi.technicaltest.networks.ApiServices
-dontwarn robi.technicaltest.networks.NetworkState$Error
-dontwarn robi.technicaltest.networks.NetworkState$Loading
-dontwarn robi.technicaltest.networks.NetworkState$Success
-dontwarn robi.technicaltest.networks.NetworkState
-dontwarn robi.technicaltest.networks.data.Repository
-dontwarn robi.technicaltest.networks.data.SearchData
-dontwarn robi.technicaltest.networks.data.User
-dontwarn robi.technicaltest.networks.di.NetworkModule
-dontwarn robi.technicaltest.networks.di.NetworkModule_ProvideGetUserDetailsUseCaseFactory
-dontwarn robi.technicaltest.networks.di.NetworkModule_ProvideGetUserRepositoriesUseCaseFactory
-dontwarn robi.technicaltest.networks.di.NetworkModule_ProvideGetUsersUseCaseFactory
-dontwarn robi.technicaltest.networks.di.NetworkModule_ProvideGitHubRepositoryFactory
-dontwarn robi.technicaltest.networks.di.NetworkModule_ProvideNetworkApiFactory
-dontwarn robi.technicaltest.networks.di.NetworkModule_ProvideSearchUsersUseCaseFactory
-dontwarn robi.technicaltest.networks.repository.GitHubRepository
-dontwarn robi.technicaltest.networks.usecase.GetUserDetailsUseCase
-dontwarn robi.technicaltest.networks.usecase.GetUserRepositoriesPagesUseCase
-dontwarn robi.technicaltest.networks.usecase.GetUserRepositoriesUseCase
-dontwarn robi.technicaltest.networks.usecase.GetUsersUseCase
-dontwarn robi.technicaltest.networks.usecase.SearchUsersUseCase