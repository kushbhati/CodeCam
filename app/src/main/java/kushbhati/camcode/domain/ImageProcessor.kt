package kushbhati.camcode.domain

import kushbhati.camcode.datamodels.GreyImage

interface ImageProcessor {
    fun blur(image: GreyImage): GreyImage
    fun clamp(image: GreyImage): GreyImage
    fun quantify(greyImage: GreyImage): List<GreyImage>
    fun analyse(quantifiedStreams: List<GreyImage>) //TODO
}