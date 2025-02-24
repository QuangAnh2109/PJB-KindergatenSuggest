//    package fa.appcode.services.impl;
//
//    import com.cloudinary.Cloudinary;
//    import com.cloudinary.utils.ObjectUtils;
//    import fa.appcode.services.CloudinaryService;
//    import org.springframework.stereotype.Service;
//
//    import java.io.IOException;
//    import java.io.InputStream;
//    import java.util.Map;
//
//
//    public class CloudinaryServiceImpl implements CloudinaryService {
//        private Cloudinary cloudinary;
//
//        public CloudinaryServiceImpl(Cloudinary cloudinary) {
//            this.cloudinary =   cloudinary;
//        }
//
//        @Override
//        public String uploadFile(InputStream inputStream) throws IOException {
//            Map uploadResult = cloudinary.uploader().upload(inputStream, ObjectUtils.asMap(
//                    "upload_preset", "ml_default",
//                    "api_key", "462197591487523",
//                    "api_secret", "7UbXkkyhKAp3eBS6A-gwx50fyIE"
//            ));
//
//            return uploadResult.get("url").toString();
//        }
//    }
