package ch.leadrian.samp.kamp.core.api.base

/**
 * Interface implemented by classes that have a model ID.
 *
 * @see [ch.leadrian.samp.kamp.core.api.entity.TextDraw.setPreviewModelId]
 * @see [ch.leadrian.samp.kamp.core.api.entity.PlayerTextDraw.setPreviewModelId]
 */
interface HasModelId {

    val modelId: Int

}