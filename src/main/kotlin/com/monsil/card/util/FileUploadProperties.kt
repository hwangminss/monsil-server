import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "file.upload")
data class FileUploadProperties(
    var main: String = "",
    var gallery: String = ""
)
