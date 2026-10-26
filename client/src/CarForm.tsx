import { useState, type FormEvent } from 'react'
import type { Car, CarInput } from './api'

interface CarFormProps {
  car?: Car
  onSave: (car: CarInput) => Promise<void>
  onCancel?: () => void
}

const toText = (value: string | number | null | undefined) => (value == null ? '' : String(value))
const toNumber = (value: string) => (value.trim() === '' ? null : Number(value))
const toNullable = (value: string) => (value.trim() === '' ? null : value.trim())

const CarForm = ({ car, onSave, onCancel }: CarFormProps) => {
  const [name, setName] = useState(toText(car?.name))
  const [model, setModel] = useState(toText(car?.model))
  const [color, setColor] = useState(toText(car?.color))
  const [year, setYear] = useState(toText(car?.year))
  const [price, setPrice] = useState(toText(car?.price))
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault()
    setSaving(true)
    setError(null)
    try {
      await onSave({
        name: name.trim(),
        model: toNullable(model),
        color: toNullable(color),
        year: toNumber(year),
        price: toNumber(price),
      })
      if (!car) {
        setName('')
        setModel('')
        setColor('')
        setYear('')
        setPrice('')
      }
    } catch (e) {
      setError(e instanceof Error ? e.message : 'No se pudo guardar')
    } finally {
      setSaving(false)
    }
  }

  return (
    <form className="car-form" onSubmit={handleSubmit}>
      <label>
        Marca
        <input value={name} onChange={(e) => setName(e.target.value)} required />
      </label>
      <label>
        Modelo
        <input value={model} onChange={(e) => setModel(e.target.value)} />
      </label>
      <label>
        Color
        <input value={color} onChange={(e) => setColor(e.target.value)} />
      </label>
      <label>
        Año
        <input type="number" min={1886} max={2100} value={year} onChange={(e) => setYear(e.target.value)} />
      </label>
      <label>
        Precio (USD)
        <input type="number" min={0} step="0.01" value={price} onChange={(e) => setPrice(e.target.value)} />
      </label>
      {error && <p className="error">{error}</p>}
      <div className="actions">
        <button type="submit" disabled={saving}>
          {saving ? 'Guardando...' : car ? 'Guardar cambios' : 'Agregar carro'}
        </button>
        {onCancel && (
          <button type="button" className="secondary" onClick={onCancel}>
            Cancelar
          </button>
        )}
      </div>
    </form>
  )
}

export default CarForm
